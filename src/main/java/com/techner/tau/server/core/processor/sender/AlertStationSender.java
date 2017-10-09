package com.techner.tau.server.core.processor.sender;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smslib.OutboundMessage;

import com.google.inject.Inject;
import com.techner.tau.server.core.mailer.MailerService;
import com.techner.tau.server.domain.model.Alert;
import com.techner.tau.server.domain.model.AlertEvent;
import com.techner.tau.server.domain.model.AlertRule;
import com.techner.tau.server.domain.model.Email;
import com.techner.tau.server.domain.model.SMS;
import com.techner.tau.server.domain.model.SMSStationEvent;
import com.techner.tau.server.domain.model.SimCard;
import com.techner.tau.server.services.AlertEventService;
import com.techner.tau.server.services.AlertRuleService;
import com.techner.tau.server.services.AlertService;
import com.techner.tau.server.services.SMSService;

/**
 * Procesador de envio de mensajes de alertas enviadas por la estacion
 * 
 * @author juan
 * 
 */
public class AlertStationSender implements SenderProcessor<SMSStationEvent> {

	/** slf4j logger. */
	private static final Logger logger = LoggerFactory.getLogger(AlertStationSender.class);
	/** Servicio SMS */
	private final SMSService smsService;
	/** Servicio de Alertas */
	private final AlertService alertService;
	/** Servicio de reglas de Alertas */
	private final AlertRuleService ruleService;
	/** Servicio de reglas de Alertas */
	private final AlertEventService eventService;
	/** Servicio de emails */
	private final MailerService mailer;

	@Inject
	public AlertStationSender(SMSService smsService, AlertService alertService, AlertRuleService ruleService,
			AlertEventService eventService, MailerService mailer) {
		this.smsService = smsService;
		this.alertService = alertService;
		this.ruleService = ruleService;
		this.eventService = eventService;
		this.mailer = mailer;
	}

	@Override
	public List<OutboundMessage> process(SMSStationEvent sms) {
		List<OutboundMessage> listSmsToSend = new ArrayList<OutboundMessage>();
		logger.debug("Procesando para enviar un EVENTO DE UNA ALERTA");
		Alert alert = alertService.getAlertByCode(sms.getAlertCode());
		if (alert != null) {
			AlertRule rule = ruleService.getAlertByCodeAndStation(sms.getStation().getId(), alert.getCode());
			if (rule != null) {
				AlertEvent event = eventService.getAlertEvent(rule.getId(), sms.getStation().getId());
				if (event != null) {
					if (eventService.isActiveEvent(event)) {
						event.setLastUpdate(new Date());
						event.setValue(Double.valueOf(sms.getValue()));
					} else {
						event = eventService.createEvent(rule, sms.getValue());
					}
				} else {
					event = eventService.createEvent(rule, sms.getValue());
				}
				eventService.saveOrUpdateAlertStation(event);
			}

			// Obtengo el texto de alerta
			StringBuffer alertText = eventService.getAlertText(alert, sms.getValue());

			if (rule.isSmsEnable()) {
				List<SimCard> sims = sms.getCustomer().getSimCards();
				if (!CollectionUtils.isEmpty(sims)) {
					for (SimCard simCard : sims) {
						listSmsToSend.add(smsService.createMessageToSend(alertText.toString(), simCard.getNumber()));
					}
				}
			}
			if (rule.isEmailEnable()) {
				List<Email> emails = sms.getCustomer().getEmails();
				Set<String> emailSet = new HashSet<String>();
				for (Email email : emails) {
					emailSet.add(email.getEmail());
				}
				String client = String.format("%s, %s", sms.getCustomer().getLastName(), sms.getCustomer().getName());
				mailer.sendWarningStationMail(emailSet, sms.getStation().getId().toString(), client,
						alertText.toString());
			}

			sms.setState(SMS.Status.COMPLETED.getCode());
		} else {
			logger.error("Error procesando alerta meteorologica de la EMA. SMS # {} set to {} ", sms.getId(),
					SMS.Status.FAILED.getCode());
			sms.setState(SMS.Status.FAILED.getCode());
		}
		return listSmsToSend;
	}
}
