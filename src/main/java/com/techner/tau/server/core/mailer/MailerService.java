package com.techner.tau.server.core.mailer;

import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ThreadPoolExecutor;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.techner.tau.common.entity.MeasureResult;
import com.techner.tau.server.core.annotation.MailerPoolExecutor;
import com.techner.tau.server.core.config.Config;
import com.techner.tau.server.core.error.LoggedNotification;
import com.techner.tau.server.domain.model.Station;

/**
 * Servicio para enviar mails usando templates de velocity
 * 
 * @author juan
 * 
 */
public class MailerService implements Mailer {

	/** slf4j logger */
	private static final Logger logger = LoggerFactory.getLogger(MailerService.class);
	/** Pool executor */
	protected final ThreadPoolExecutor exec;
	/** Configuracion */
	private final Config config;
	/** Patter para formateo de fechas */
	private static final String DATE_PATTERN = "HH:mm:ss 'del día' dd/MM/yyyy ";
	/** Patter para formateo de fechas */
	private static final String DATE_PATTERN_SHORT = "dd/MM/yyyy";
	/** Factoria de objetos para enviar mails */
	private final MailJobFactory factory;
	/** key a ser remplazada por el mensaje que se va a mostrar del mail */
	private static final String MESSAGE_FIELD = "message";
	/** key a ser remplazada por el mensaje que se va a mostrar del mail */
	private static final String FILE_FIELD = "file";
	/** key a ser remplazada por el mensaje que se va a mostrar del mail */
	private static final String LINE_NUMBER_FIELD = "line";
	/** key a ser remplazada por el mensaje que se va a mostrar del mail */
	private static final String METHOD_FIELD = "method";
	/** key a ser remplazada por el mensaje que se va a mostrar del mail */
	private static final String CLASS_FIELD = "class";
	/** key a ser remplazada por el mensaje que se va a mostrar del mail */
	private static final String STACKTRACE_FIELD = "stacktrace";
	/** key a ser remplazada por el hostname */
	private static final String HOSTNAME_FIELD = "hostname";
	/** key a ser remplazada por el ambiente */
	private static final String ENVIRONMENT_FIELD = "environment";
	/** key a ser remplazada por la estacion que se va a mostrar del mail */
	private static final String STATION_FIELD = "station";
	/** key a ser remplazada por el cliente que se va a mostrar del mail */
	private static final String CLIENT_FIELD = "client";
	/** key a ser remplazada por el nombre de las variables */
	private static final String VARIABLE_FIELD = "variable";
	/** key a ser remplazada por el valor de las variables */
	private static final String VALUE_ACTUAL_FIELD = "actual";
	/** key a ser remplazada por el valor de las variables */
	private static final String VALUE_AVG_FIELD = "avg";
	/** key a ser remplazada por el valor de las variables */
	private static final String VALUE_MAX_FIELD = "max";
	/** key a ser remplazada por el valor de las variables */
	private static final String VALUE_MIN_FIELD = "min";
	/** key a ser remplazada por la lista de varibales y valores */
	private static final String LIST_MEASURES = "listMeasures";
	/** key a ser remplazada por la fecha de las medidas */
	private static final String DATE = "date";
	/** subject prefix**/
	private static final String SUBJECT = "[TAU]";
	/** Velocity Engine */
	private final VelocityEngine ve;
	/** Empty result */
	private final String EMPTY_RESULT = "   -   ";

	@Inject
	public MailerService(Config config, MailJobFactory factory, VelocityEngine ve,
			@MailerPoolExecutor ThreadPoolExecutor exec) {
		this.config = config;
		this.factory = factory;
		this.exec = exec;
		this.ve = ve;
	}

	@Override
	public void sendCriticalStationMail(Set<String> emails, String station, String client, String error)
			throws ResourceNotFoundException, ParseErrorException {
		String content = createEmailStationContent(config.getErrorEmailTemplate(), error, station, client);
		MailJob job = factory.create(emails, content, config.getErrorEmailSubject() + station);
		exec.execute(job);
	}

	@Override
	public void sendCriticalStationMail(String email, String station, String client, String error)
			throws ResourceNotFoundException, ParseErrorException {
		Set<String> emails = new HashSet<String>(Arrays.asList(email));
		sendCriticalStationMail(emails, station, client, error);
	}
	

	@Override
	public void sendWarningStationMail(String email, String station, String client, String error)
	       throws ResourceNotFoundException, ParseErrorException {
            Set<String> emails = new HashSet<String>(Arrays.asList(email));
            sendWarningStationMail(emails, station, client, error);
	}

	@Override
	public void sendWarningStationMail(Set<String> emails, String station, String client, String warn)
			throws ResourceNotFoundException, ParseErrorException {
		String content = createEmailStationContent(config.getWarningEmailTemplate(), warn, station, client);
		MailJob job = factory.create(emails, content, config.getWarningEmailSubject() + station);
		exec.execute(job);
	}

	@Override
	public void sendCustomStationMail(Set<String> emails, Station station, String client, String text, String subject)
	            throws ResourceNotFoundException, ParseErrorException {
            String content = createEmailStationContent(config.getWarningEmailTemplate(), text, station.getId().toString() + "- " 
	            + station.getInformation().getLocation(), client);
            MailJob job = factory.create(emails, content, SUBJECT + " ["+ subject +"] - "+ station.getInformation().getLocation());
            exec.execute(job);
	}
	
	@Override
	public void sendInformationStationMail(Set<String> emails, String station, String client, String info)
			throws ResourceNotFoundException, ParseErrorException {
		String content = createEmailStationContent(config.getInformationEmailTemplate(), info, station, client);
		MailJob job = factory.create(emails, content, config.getInformationEmailSubject() + station);
		exec.execute(job);
	}

	@Override
	public void sendPeriodicNotificationEmail(Set<String> emails, List<MeasureResult> measures, Station station) {
		String date = getFormatedDate(new Date(), DATE_PATTERN_SHORT);
		String content = createPeriodicalNotificationEmailContent(config.getPeriodicalMesauresEmailTemplate(),
				measures, station);
		StringBuffer sb = new StringBuffer(config.getPeriodicalMesauresEmailSubject()).append(" ")
				.append(station.getInformation().getLocation()).append(" - Fecha: ").append(date);
		MailJob job = factory.create(emails, content, sb.toString());
		exec.execute(job);
	}

	@Override
	public void sendErrorReportMail(Throwable e) {
		Set<String> emails = new HashSet<String>();
		emails.add(config.getEmailToReportError());
		LoggedNotification notification = new LoggedNotification(e);
		StringBuffer subject = new StringBuffer(config.getErrorReportEmailSubject()).append(notification.getMessage());
		String content = createErrorReport(config.getErrorReportTempale(), notification);
		MailJob job = factory.create(emails, content, subject.toString());
		exec.execute(job);
	}

	/**
	 * Obtiene el template que corresponde con el nombre template
	 * 
	 * @param template
	 *            nombre del template
	 * @return template
	 */
	private Template getTemplate(String template) {
		Template t = null;
		try {
			t = ve.getTemplate(template);
		} catch (ResourceNotFoundException e) {
			logger.error("No se encuentra el template en la ubicacion dada {}", template, e);
			throw e;
		} catch (ParseErrorException e) {
			logger.error("Error de sintaxis en el template ubicado en {}", template, e);
			throw e;
		}
		return t;
	}

	/**
	 * Crea el mail remplazando la key por el contenido del parametro content
	 * 
	 * @param template
	 *            velocity template a utilizar
	 * @param content
	 *            contenido a insertar
	 * @return mail en formato final
	 * @throws ResourceNotFoundException
	 *             template imposible de ubicar
	 * @throws ParseErrorException
	 *             error en la sintaxis del template
	 */
	private final String createEmailStationContent(String template, String content, String station, String client)
			throws ResourceNotFoundException, ParseErrorException {
		Template t = getTemplate(template);
		VelocityContext context = new VelocityContext();
		context.put(MESSAGE_FIELD, content);
		context.put(STATION_FIELD, station);
		context.put(CLIENT_FIELD, client);
		StringWriter writer = new StringWriter();
		t.merge(context, writer);
		return writer.toString();
	}

	/**
	 * Crea el contenido del email a partir de la informaciòn de las variables
	 * tomadas
	 * 
	 * @param template
	 *            template
	 * @param measures
	 *            medidas
	 * @param station
	 *            estacion
	 * @return contenido del mail
	 */
	private String createPeriodicalNotificationEmailContent(String template, List<MeasureResult> measures,
			Station station) {
		Template t = getTemplate(template);
		VelocityContext context = new VelocityContext();
		context.put(STATION_FIELD, String.format("%s - %s", station.getId(), station.getInformation().getLocation()));
		context.put(CLIENT_FIELD,
				String.format("%s, %s", station.getCustomer().getLastName(), station.getCustomer().getName()));
		context.put(DATE, getFormatedDate(new Date(), DATE_PATTERN));
		List<Map<String, String>> list = createMeasureListMap(measures);
		context.put(LIST_MEASURES, list);
		StringWriter writer = new StringWriter();
		t.merge(context, writer);
		return writer.toString();
	}

	/***
	 * Crea el contenido del reporte de errores
	 * 
	 * @param template
	 *            template de errores
	 * @param t
	 *            hilo que origina el error
	 * @param e
	 *            exception
	 * @return contenido
	 */
	private String createErrorReport(String template, LoggedNotification notification) {
		Template temp = getTemplate(template);
		VelocityContext context = new VelocityContext();
		context.put(DATE, new Date());
		context.put(MESSAGE_FIELD, notification.getMessage());
		context.put(FILE_FIELD, notification.getFile());
		context.put(LINE_NUMBER_FIELD, notification.getLineNumber());
		context.put(METHOD_FIELD, notification.getMethod());
		context.put(CLASS_FIELD, notification.getClassName());
		context.put(STACKTRACE_FIELD, notification.getStackTrace());
		context.put(HOSTNAME_FIELD, config.getHostname());
		context.put(ENVIRONMENT_FIELD, config.getEnvironment());
		StringWriter writer = new StringWriter();
		temp.merge(context, writer);
		return writer.toString();
	}

	/**
	 * Obtiene la fecha formateada
	 * 
	 * @param measures
	 *            medidas
	 * @param format
	 *            date format
	 * @return fecha formateada
	 */
	private String getFormatedDate(Date date, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(date);
	}

	/**
	 * Crea un mapa de medidas para que Velocity las entienda
	 * 
	 * @param measures
	 *            medidas
	 * @return mapa
	 */
	private List<Map<String, String>> createMeasureListMap(List<MeasureResult> measures) {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		for (MeasureResult measure : measures) {
			String unit = (StringUtils.isBlank(measure.getVariable().getUnit()) ? "" : measure.getVariable().getUnit());
			Map<String, String> map = new HashMap<String, String>();
			map.put(VARIABLE_FIELD, measure.getVariable().getName());
			if (CollectionUtils.isNotEmpty(measure.getValues()) && measure.getValues().get(0) != null) {
				com.techner.tau.common.entity.Measure values = measure.getValues().get(0);
				map.put(VALUE_ACTUAL_FIELD,
						(values.getValue() != null) ? String.format("%s %s", values.getValue(), unit) : EMPTY_RESULT);
				map.put(VALUE_AVG_FIELD, (values.getAvg() != null) ? String.format("%s %s", values.getAvg(), unit)
						: EMPTY_RESULT);
				map.put(VALUE_MAX_FIELD, (values.getMax() != null) ? String.format("%s %s", values.getMax(), unit)
						: EMPTY_RESULT);
				map.put(VALUE_MIN_FIELD, (values.getMin() != null) ? String.format("%s %s", values.getMin(), unit)
						: EMPTY_RESULT);
			}
			list.add(map);
		}
		return list;
	}
}
