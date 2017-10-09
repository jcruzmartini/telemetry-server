package com.techner.tau.server.core.periodicalnotifications;

import java.rmi.ServerException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smslib.OutboundMessage;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;
import com.techner.tau.common.entity.ActualMeasuresResult;
import com.techner.tau.common.entity.Measure;
import com.techner.tau.common.entity.MeasureResult;
import com.techner.tau.common.entity.ServiceResult;
import com.techner.tau.common.entity.TAUActualMeasuresResult;
import com.techner.tau.common.entity.TAUServiceResult;
import com.techner.tau.common.entity.Variable;
import com.techner.tau.server.core.annotation.EndpointURL;
import com.techner.tau.server.core.annotation.TokenForServices;
import com.techner.tau.server.core.mailer.MailerService;
import com.techner.tau.server.domain.model.Email;
import com.techner.tau.server.domain.model.PeriodicalNotification;
import com.techner.tau.server.domain.model.SimCard;
import com.techner.tau.server.domain.model.Station;
import com.techner.tau.server.services.SMSService;

/**
 * 
 * @author juan
 * 
 */
public class PeriodicalNotificationsSenderTask implements PeriodicalNotificationsSender {
	/** path del servicio a llamar */
	private static final String SERVICE = "measures/%s/actual?eToken=%s";
	/** parentesis para abrir */
	private final String OPEN_PARENTHESIS = "(";
	/** parentesis para cerrar */
	private final String CLOSE_PARENTHESIS = ")";
	/** separador */
	private final String SEPARATOR = " - ";
	/** slf4j logger */
	private static final Logger logger = LoggerFactory.getLogger(PeriodicalNotificationsSenderTask.class);
	/** Mail service */
	private final MailerService mailer;
	/** station */
	private final PeriodicalNotification notification;
	/** Jersey Client */
	private final Client client;
	/** SMS service **/
	private final SMSService smsService;
	/** endpoint URL */
	private final String endpoint;
	/** Token for services **/
	private final String token;

	/**
	 * Constructor
	 * 
	 * @param mailer
	 *            mailer service
	 * @param service
	 *            measure service
	 * @param notification
	 *            stations notification information
	 * @param endpoint
	 *            endpoint
	 */
	@Inject
	public PeriodicalNotificationsSenderTask(MailerService mailer, Client client,
			@Assisted PeriodicalNotification notification, SMSService smsService, @EndpointURL String endpoint,
			@TokenForServices String token) {
		this.mailer = mailer;
		this.client = client;
		this.notification = notification;
		this.smsService = smsService;
		this.endpoint = endpoint;
		this.token = token;
	}

	@Override
	public void run() {
		Station station = notification.getStation();

		ActualMeasuresResult actual = null;
		try {
			actual = callRESTService(station.getId());
		} catch (ServerException e) {
			return;
		}

		if (actual != null && CollectionUtils.isNotEmpty(actual.getEntries())) {
			if (notification.isEmailEnable()) {
				logger.info("Enviando notificaciones periodicas via Email para la estacion # {}", station.getId());
				List<Email> emailsObj = station.getCustomer().getEmails();
				mailer.sendPeriodicNotificationEmail(createEmailSet(emailsObj), actual.getEntries(), station);
			}

			if (notification.isSmsEnable()) {
				List<OutboundMessage> messages = new ArrayList<OutboundMessage>();
				String text = createSMSDailyReportText(actual.getEntries());
				List<SimCard> simCards = station.getCustomer().getSimCards();

				logger.info("Enviando notificaciones periodicas via SMS para la estacion # {} con texto {}",
						station.getId(), text);

				for (SimCard simCard : simCards) {
					messages.add(smsService.createMessageToSend(text, simCard.getNumber()));
				}
				smsService.sendSMS(messages);
			}
		} else {
			logger.error(
					"No hay medidas disponibles para ser enviadas por las notificaciones periodicas para la estación # {}",
					station.getId());
		}
	}

	/**
	 * Crea el contenido del reporte diario via SMS
	 * 
	 * @param entries
	 *            lista de medidas
	 * @return texto del reporte
	 */
	private String createSMSDailyReportText(List<MeasureResult> entries) {
		StringBuffer sb = new StringBuffer("Reporte Diario: ");

		for (MeasureResult measure : entries) {
			Variable var = measure.getVariable();

			// Omitimos las variables calculadas para el reporte diario de SMS
			if (var.getCalculated() != null && var.getCalculated() == 0) {
				Measure value = measure.getValues().get(0);
				String val = (value.getValue() == null) ? StringUtils.EMPTY : value.getValue().toString();
				String max = (value.getMax() == null) ? StringUtils.EMPTY : value.getMax().toString();
				String min = (value.getMin() == null) ? StringUtils.EMPTY : value.getMin().toString();
				String unit = (var.getUnit() == null) ? StringUtils.EMPTY : var.getUnit();
				if (StringUtils.isEmpty(val) || StringUtils.isEmpty(max) || StringUtils.isEmpty(min)) {
					continue;
				}
				if (sb.length() > 16) {
					sb.append(SMSService.COMA_SEPARATOR);
				}
				sb.append(var.getSmsDescription()).append(SMSService.BLANK_SPACE).append(val).append(unit)
						.append(SMSService.BLANK_SPACE).append(OPEN_PARENTHESIS).append(min).append(SEPARATOR)
						.append(max).append(CLOSE_PARENTHESIS);
			}
		}

		return sb.toString();
	}

	/**
	 * Crea set de emails a partir de los objetos
	 * 
	 * @param emailsObj
	 *            objetos de email
	 * @return emails
	 */
	private Set<String> createEmailSet(List<Email> emailsObj) {
		Set<String> emails = new HashSet<String>();
		for (Email email : emailsObj) {
			emails.add(email.getEmail());
		}
		return emails;
	}

	/**
	 * Construye mapa con las variables y sus valores
	 * 
	 * @param id
	 *            id de la estacion
	 * @return resultados
	 * @throws ServerException
	 */
	private ActualMeasuresResult callRESTService(Integer id) throws ServerException {
		ClientResponse response = null;
		ServiceResult<TAUActualMeasuresResult> result = null;
		ActualMeasuresResult actual = null;

		StringBuffer sb = new StringBuffer(endpoint).append(String.format(SERVICE, String.valueOf(id), token));

		logger.debug("LLAMADA AL SERVICIO :" + sb.toString());

		try {
			WebResource resource = client.resource(sb.toString());
			response = resource.accept(MediaType.APPLICATION_JSON_TYPE).get(ClientResponse.class);
		} catch (Exception e) {
			logger.error("Error llamando al servicio {} ", sb.toString(), e);
			throw new ServerException("Error llamando al servicio {} " + sb.toString());
		}

		if (response != null && response.getStatus() == Response.Status.OK.getStatusCode()) {
			GenericType<TAUServiceResult<TAUActualMeasuresResult>> type = new GenericType<TAUServiceResult<TAUActualMeasuresResult>>() {
			};
			result = response.getEntity(type);
			if (result.isSuccess()) {
				actual = result.getResult();
			}
		} else {
			logger.error("Error ejecutando servicios. {}", response);
			throw new ServerException("Error en llamada al servicio. Respuesta : " + response);
		}
		logger.debug("RESPUESTA DEL SERVICIO : {} ", response);
		return actual;
	}

}
