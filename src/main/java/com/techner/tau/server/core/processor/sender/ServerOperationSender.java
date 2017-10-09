package com.techner.tau.server.core.processor.sender;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smslib.OutboundMessage;

import com.google.inject.Inject;
import com.techner.tau.server.domain.model.SMSServerOperation;
import com.techner.tau.server.services.SMSService;

/**
 * Procesador de envio de mensajes de operaciones a la estacion
 * 
 * @author juan
 * 
 */
public class ServerOperationSender implements SenderProcessor<SMSServerOperation> {

	/** slf4j logger. */
	private static final Logger logger = LoggerFactory.getLogger(ServerOperationSender.class);
	/** Servicio SMS */
	private final SMSService smsService;

	@Inject
	public ServerOperationSender(SMSService smsService) {
		this.smsService = smsService;
	}

	@Override
	public List<OutboundMessage> process(SMSServerOperation sms) {
		List<OutboundMessage> listSmsToSend = new ArrayList<OutboundMessage>();
		logger.debug("Procesando para enviar  -  Mensaje de Operacion hacia la TAU");
		// Proceso para setear token de la operacion
		smsService.postProcessSMS(sms);
		listSmsToSend.add(smsService.createMessageToSend(sms.getText(), sms.getStation().getSimCard().getNumber()));
		return listSmsToSend;
	}

}
