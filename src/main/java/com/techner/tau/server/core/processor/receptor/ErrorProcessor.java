package com.techner.tau.server.core.processor.receptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smslib.InboundMessage;

import com.google.inject.Inject;
import com.techner.tau.server.core.config.Config;
import com.techner.tau.server.domain.interfaces.Message;
import com.techner.tau.server.domain.model.Error;
import com.techner.tau.server.domain.model.SMSError;
import com.techner.tau.server.domain.model.Station;
import com.techner.tau.server.services.ErrorService;
import com.techner.tau.server.services.SMSService;
import com.techner.tau.server.services.StationService;

/**
 * Procesador encargado de la recepcion de mensajes de error por parte de la
 * estacion
 * 
 * @author juan
 * 
 */
public class ErrorProcessor implements ReceptorProcessor {

	/** slf4j logger */
	private static final Logger logger = LoggerFactory.getLogger(ErrorProcessor.class);
	/** Servicio de estaciones */
	private final StationService stationService;
	/** Servicio de Errores */
	private final ErrorService errorService;
	/** Servicio de SMS */
	private final SMSService smsService;
	/** Codigo generico de error */
	private final String genericErrorCode;

	@Inject
	public ErrorProcessor(SMSService smsService, StationService stationService, ErrorService errorService, Config config) {
		this.stationService = stationService;
		this.errorService = errorService;
		this.smsService = smsService;
		this.genericErrorCode = config.getGenericErrorCode();
	}

	@Override
	public Message process(InboundMessage inboundMessage) {
		logger.debug("Procesando mensaje de error");
		SMSError sms = new SMSError(inboundMessage);

		String[] textSplited = inboundMessage.getText().split(Config.SPLIT_CHARACTER_GENERAL);
		if (textSplited == null || textSplited.length < 2) {
			logger.error("Error de formato de mensaje enviado por la EMA para informar de un ERROR. Texto: {}",
					inboundMessage.getText());
		}

		// Almaceno el error para desp informarlo
		Station station = stationService.getStationBySimNumber(inboundMessage.getOriginator());
		if (station != null) {
			logger.info("Guardando error recibido de la EMA con simCard #{}", inboundMessage.getOriginator());
			Error error = errorService.getErrorByCode(textSplited[1]);
			// Si es nulo le seteamos el error generico
			if (error == null) {
				error = errorService.getErrorByCode(genericErrorCode);
			}
			sms.setError(error);
			sms.setStation(station);
			smsService.saveOrUpdateSMS(sms);
		}
		return sms;
	}

}
