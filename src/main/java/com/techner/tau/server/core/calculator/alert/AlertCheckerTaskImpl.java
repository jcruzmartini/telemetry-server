package com.techner.tau.server.core.calculator.alert;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.techner.tau.server.core.mailer.MailerService;
import com.techner.tau.server.data.dao.interfaces.AlertDao;
import com.techner.tau.server.domain.model.AlertEvent;
import com.techner.tau.server.domain.model.AlertRule;
import com.techner.tau.server.domain.model.Email;
import com.techner.tau.server.domain.model.Measure;
import com.techner.tau.server.domain.model.SimCard;
import com.techner.tau.server.domain.model.Station;
import com.techner.tau.server.services.AlertEventService;
import com.techner.tau.server.services.AlertRuleService;
import com.techner.tau.server.services.SMSService;

/**
 * Trabajo de chequeo de alertas para cada estación
 * 
 * @author jmartini
 * 
 */
public class AlertCheckerTaskImpl implements AlertCheckerTask {
	/** Id de la estacion a calcular la alerta **/
	private final Station station;
	/** Alert rule service */
	private final AlertRuleService alertRuleService;
	/** Alert event service */
	private final AlertEventService alertEventService;
	/** lista de medidas */
	private final List<Measure> listMeasures;
	/** Servicio de emails */
	private final MailerService mailer;
	/** Servicio sms **/
	private final SMSService smsService;
	/** logger */
	private static final Logger logger = LoggerFactory.getLogger(AlertCheckerTaskImpl.class);

	@Inject
	public AlertCheckerTaskImpl(@Assisted Station station, @Assisted List<Measure> listMeasures,
			AlertRuleService alertRuleService, AlertEventService alertEventService, MailerService mailer,
			SMSService smsService) {
		this.station = station;
		this.listMeasures = listMeasures;
		this.alertRuleService = alertRuleService;
		this.alertEventService = alertEventService;
		this.mailer = mailer;
		this.smsService = smsService;
	}

	@Override
	public void run() {
		logger.info("Chequeando alertas para las estacion {}.", station.getId());
		List<AlertRule> rules = alertRuleService
				.findActiveAlertRulesByChecker(station.getId(), AlertDao.CHECKER_SERVER);
		if (CollectionUtils.isEmpty(rules)) {
			return;
		}
		logger.info("Reglas de Alertas ACTIVAS encontradas. {}. ", rules);

		Map<String, Double> map = buildMapCodeValue(listMeasures);
		for (AlertRule rule : rules) {
			String code = rule.getAlert().getVariable().getCode();
			if (!map.containsKey(code)) {
				continue;
			}
			Double value = map.get(code);
			String strValue = String.valueOf(value);
			boolean inAlarm = isInAlarm(value, rule.getMin(), rule.getMax());
			if (inAlarm) {
				logger.info("EVENTO DE ALARMA ENCONTRADO para variable {}. ", code);
				AlertEvent event = alertEventService.getAlertEvent(rule.getId(), station.getId());
				if (event != null) {
					boolean isActive = alertEventService.isActiveEvent(event);
					if (isActive) {
						event.setLastUpdate(new Date());
						event.setValue(value);
					} else {
						event = alertEventService.createEvent(rule, strValue);
					}
				} else {
					event = alertEventService.createEvent(rule, strValue);
				}
				alertEventService.saveOrUpdateAlertStation(event);
				// Obtengo el texto de alerta
				StringBuffer alertText = alertEventService.getAlertText(rule.getAlert(), strValue);
				String client = String.format("%s, %s", station.getCustomer().getLastName(), station.getCustomer()
						.getName());
				if (rule.isEmailEnable()) {
					logger.info("Enviando mails de alarma a cliente {}. ", client);
					List<Email> emails = station.getCustomer().getEmails();
					Set<String> emailSet = new HashSet<String>();
					for (Email email : emails) {
						emailSet.add(email.getEmail());
					}
					mailer.sendCustomStationMail(emailSet, station, client, alertText.toString(), alertText.toString());
				}

				if (rule.isSmsEnable()) {
					List<SimCard> simcards = station.getCustomer().getSimCards();
					logger.info("Enviando {} SMS de alarma a cliente {}. ", (simcards != null) ? simcards.size() : 0,
							client);
					for (SimCard simCard : simcards) {
						smsService.sendMessageTo(alertText.toString(), simCard.getNumber());
					}
				}
			}
		}

	}

	/**
	 * Chequear si esta en alarma
	 * 
	 * @param value
	 *            valor de la mediad
	 * @param minminimo
	 *            tolerable
	 * @param max
	 *            maximo tolerable
	 * @return true en caso de alarma, false en otro caso
	 */
	private boolean isInAlarm(Double value, Double min, Double max) {
		if (value > max || value < min) {
			return true;
		}
		return false;
	}

	/**
	 * Construir mapa de codigos de variable y valores
	 * 
	 * @param listMeasures
	 *            lista de medidas
	 * @return mapa de var y valores
	 */
	private Map<String, Double> buildMapCodeValue(List<Measure> listMeasures) {
		Map<String, Double> varMap = new HashMap<String, Double>();
		for (Measure measure : listMeasures) {
			Double value = measure.getValue();
			if (value != null) {
				varMap.put(measure.getVariable().getCode(), value);
			}
		}
		return varMap;
	}

}
