package com.techner.tau.server.core.processor.receptor;

import java.util.Date;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smslib.InboundMessage;

import com.google.inject.Inject;
import com.techner.tau.server.core.config.Config;
import com.techner.tau.server.core.processor.operation.PostOperationProcessor;
import com.techner.tau.server.domain.interfaces.Message;
import com.techner.tau.server.domain.model.Response;
import com.techner.tau.server.domain.model.SMS;
import com.techner.tau.server.domain.model.SMSServerOperation;
import com.techner.tau.server.services.ResponseService;
import com.techner.tau.server.services.SMSService;

/**
 * Procesador encargado de la recepcion de mensajes de respuesta a una operacion
 * enviada por el sevidor
 * 
 * @author juan
 * 
 */
public class OperationResponseProcessor implements ReceptorProcessor {

	/** slf4j logger */
	private static final Logger logger = LoggerFactory.getLogger(OperationResponseProcessor.class);
	/** Servicio de SMS */
	private final SMSService smsService;
	/** Servicio de Respuestas */
	private final ResponseService responseService;
	/** post procesadores de operaciones */
	private final Map<String, PostOperationProcessor> processors;

	@Inject
	public OperationResponseProcessor(SMSService smsService, ResponseService responseService,
			Map<String, PostOperationProcessor> processors) {
		this.smsService = smsService;
		this.responseService = responseService;
		this.processors = processors;
	}

	@Override
	public Message process(InboundMessage inboundMessage) {
		logger.info("Procesando MENSAJE DE RESPUESTA A OPERACION POR PARTE DEL SERVIDOR");
		String[] textSplited = inboundMessage.getText().split(Config.SPLIT_CHARACTER_GENERAL);
		if (textSplited == null || textSplited.length < 3) {
			logger.error(
					"Error de formato de mensaje enviado por la ETA para confirmar operacion del server. Texto: {}",
					inboundMessage.getText());
		}
		Integer tokenNumber = smsService.getTokenNumber(inboundMessage.getText());
		SMSServerOperation sms = smsService.getSMSOperationWaitingForResponse(inboundMessage.getOriginator(),
				tokenNumber);
		if (sms == null) {
			logger.error(
					"No hay sms de operacion en estado pendiente de respuesta de la EMA con simCard #{} y token {}",
					inboundMessage.getOriginator(), tokenNumber);
			return sms;
		}

		Response response = responseService.getResponseByValue(textSplited[1]);
		if (response != null && response.isSuccess()) {
			sms.setDelivered_at(new Date());
			sms.setState(SMS.Status.COMPLETED.getCode());
			sms.setSuccess(response.isSuccess());
		} else {
			sms.setErrorCode(textSplited[1]);
			sms.setState(SMS.Status.FAILED.getCode());
			logger.error("Error proveniente de la acciòn con token {} enviada a la EMA {}", tokenNumber,
					inboundMessage.getOriginator());
		}

		postProcessOperation(sms);
		smsService.saveOrUpdateSMS(sms);
		return sms;
	}

	/**
	 * Post procesa la operacion y realiza lo que corresponda
	 * 
	 * @param sms
	 *            operation
	 */
	private void postProcessOperation(SMSServerOperation sms) {
		String[] text = StringUtils.split(sms.getText(), Config.GENERIC_SEPARATOR);
		if (text != null) {
			String operation = text[0];
			if (StringUtils.isNotBlank(operation)) {
				PostOperationProcessor postPro = processors.get(operation);
				if (postPro != null) {
					boolean success = postPro.postProcessOperation(sms);
					logger.info("Operacion del tipo [{}] post procesada con resultado [{}]", operation, success);
				}
			}
		}
	}
}
