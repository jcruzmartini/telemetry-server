package com.techner.tau.server.core.processor.sender;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smslib.OutboundMessage;

import com.google.inject.Inject;
import com.techner.tau.server.core.mailer.Mailer;
import com.techner.tau.server.domain.model.EventStatus;
import com.techner.tau.server.domain.model.SMS;
import com.techner.tau.server.domain.model.SMSStatus;
import com.techner.tau.server.domain.model.User;
import com.techner.tau.server.services.EventStatusService;
import com.techner.tau.server.services.SMSService;
import com.techner.tau.server.services.UserService;

public class StatusEventSender implements SenderProcessor<SMSStatus> {

	/** slf4j logger. */
	private static final Logger logger = LoggerFactory.getLogger(StatusEventSender.class);
	/** Servicio SMS */
	private final SMSService smsService;
	/** Servicio de usuarios */
	private final UserService userService;
	/** Servicios de Mail */
	private final Mailer mailerService;

	@Inject
	public StatusEventSender(SMSService smsService, EventStatusService eventService, UserService userService,
			Mailer mailerService) {
		this.smsService = smsService;
		this.userService = userService;
		this.mailerService = mailerService;
	}

	@Override
	public List<OutboundMessage> process(SMSStatus sms) {
		List<OutboundMessage> listSmsToSend = new ArrayList<OutboundMessage>();
		logger.debug("Procesando para enviar  -  Mensaje de Evento critico en la estacion");
		EventStatus event = sms.getEvent();
		boolean isCritical = event.isCritical();
		List<User> users = userService.getActiveUsers();
		if (event != null && !CollectionUtils.isEmpty(users)) {
			StringBuffer alertText = new StringBuffer(event.getDescription());
			Set<String> emails = new HashSet<String>();
			for (User user : users) {
				emails.add(user.getEmail());
				if (isCritical) {
					if (user.getSimCard() != null) {
						listSmsToSend.add(smsService.createMessageToSend(alertText.toString(), user.getSimCard()
								.getNumber()));
					}
				}
			}

			// Envio de mails de error a los usuarios habilitados
			StringBuffer station = new StringBuffer(sms.getStation().getId().toString()).append(" - ").append(
					sms.getStation().getSimCard().getNumber());
			StringBuffer client = new StringBuffer(sms.getStation().getCustomer().getLastName()).append(", ").append(
					sms.getStation().getCustomer().getName());
			try {
				if (isCritical) {
					mailerService.sendCriticalStationMail(emails, station.toString(), client.toString(),
							alertText.toString());
				} else {
					mailerService.sendWarningStationMail(emails, station.toString(), client.toString(),
							alertText.toString());
				}
			} catch (Exception ex) {
				logger.error("Error enviando mails de error", ex);
			}
			sms.setState(SMS.Status.COMPLETED.getCode());
		} else {
			logger.error("Error procesando mensaje de estado critico de EMA . SMS # {} Estado en  {} ", sms.getId(),
					SMS.Status.FAILED.getCode());
			logger.error("Alerta {}. Usuarios {}", event, users);
			sms.setState(SMS.Status.FAILED.getCode());
		}
		return listSmsToSend;
	}

}
