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
import com.techner.tau.server.domain.model.SMS;
import com.techner.tau.server.domain.model.SMSError;
import com.techner.tau.server.domain.model.User;
import com.techner.tau.server.services.SMSService;
import com.techner.tau.server.services.UserService;

/**
 * Procesador de envio de mensajes de error enviado por parte de la estacion
 * Envia mails a los admins informando del error
 * 
 * @author juan
 * 
 */
public class ErrorSender implements SenderProcessor<SMSError> {

	/** slf4j logger. */
	private static final Logger logger = LoggerFactory.getLogger(ResponseSender.class);
	/** Servicio SMS */
	private final SMSService smsService;
	/** Servicio de usuarios */
	private final UserService userService;
	/** Servicios de Mail */
	private final Mailer mailerService;

	@Inject
	public ErrorSender(SMSService smsService, UserService userService, Mailer mailerService) {
		this.smsService = smsService;
		this.userService = userService;
		this.mailerService = mailerService;
	}

	@Override
	public List<OutboundMessage> process(SMSError sms) {
		List<OutboundMessage> listSmsToSend = new ArrayList<OutboundMessage>();
		logger.debug("Procesando para enviar mensajes de ERROR");

		List<User> users = userService.getActiveUsers();
		if (!CollectionUtils.isEmpty(users)) {
			StringBuffer e = new StringBuffer("Error grave con codigo: ").append(sms.getError().getCodError())
					.append(". Descripcion: ").append(sms.getError().getDescription()).append(" .EMA # ")
					.append(sms.getStation().getId());
			Set<String> emails = new HashSet<String>();
			for (User user : users) {
				emails.add(user.getEmail());
				if (user.getSimCard() != null) {
					listSmsToSend.add(smsService.createMessageToSend(e.toString(), user.getSimCard().getNumber()));
				}
			}
			// Envio de mails de error a los usuarios habilitados
			StringBuffer texto = new StringBuffer("Cod. Error :").append(sms.getError().getCodError()).append("\n")
					.append(". Descripcion: ").append(sms.getError().getDescription());
			StringBuffer station = new StringBuffer(sms.getStation().getId()).append(" - ").append(
					sms.getStation().getSimCard().getNumber());
			StringBuffer client = new StringBuffer(sms.getStation().getCustomer().getName()).append(", ").append(
					sms.getStation().getCustomer().getLastName());
			try {
				mailerService.sendCriticalStationMail(emails, station.toString(), client.toString(), texto.toString());
			} catch (Exception ex) {
				logger.error("Error enviando mails de error", ex);
			}
			sms.setState(SMS.Status.COMPLETED.getCode());
		} else {
			logger.error("No existen usuarios administradores para enviar reporte de error");
			sms.setState(SMS.Status.FAILED.getCode());
		}
		return listSmsToSend;
	}
}
