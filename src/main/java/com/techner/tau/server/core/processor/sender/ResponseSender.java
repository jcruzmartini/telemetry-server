package com.techner.tau.server.core.processor.sender;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smslib.OutboundMessage;

import com.google.inject.Inject;
import com.techner.tau.server.domain.model.SMS;
import com.techner.tau.server.domain.model.SMSResponse;
import com.techner.tau.server.services.SMSService;

/**
 * Procesador de envio de mensajes de respuesta a una solicitud de de datos por
 * parte del cliente
 * 
 * @author juan
 * 
 */
public class ResponseSender implements SenderProcessor<SMSResponse> {

	/** slf4j logger. */
	private static final Logger logger = LoggerFactory.getLogger(ResponseSender.class);
	private final SMSService smsService;

	@Inject
	public ResponseSender(SMSService smsService) {
		this.smsService = smsService;
	}

	@Override
	public List<OutboundMessage> process(SMSResponse sms) {
		List<OutboundMessage> listSmsToSend = new ArrayList<OutboundMessage>();
		logger.debug("Processing to Send -  RESPONSE TO REQUEST message");
		listSmsToSend.add(smsService.createMessageToSend(sms.getFriendlyText(), sms.getTo()));
		sms.setState(SMS.Status.COMPLETED.getCode());
		return listSmsToSend;
	}

}
