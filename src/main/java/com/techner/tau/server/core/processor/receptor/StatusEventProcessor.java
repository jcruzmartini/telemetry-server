package com.techner.tau.server.core.processor.receptor;

import java.text.ParseException;
import java.util.Date;

import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smslib.InboundMessage;

import com.google.inject.Inject;
import com.techner.tau.server.core.config.Config;
import com.techner.tau.server.domain.interfaces.Message;
import com.techner.tau.server.domain.model.EventStatus;
import com.techner.tau.server.domain.model.SMSStatus;
import com.techner.tau.server.domain.model.Station;
import com.techner.tau.server.services.EventStatusService;
import com.techner.tau.server.services.SMSService;
import com.techner.tau.server.services.StationService;

/**
 * Procesador encargado de la recepcion de mensajes de alerta de estado critico
 * de una estacion
 * 
 * @author juan
 * 
 */
public class StatusEventProcessor implements ReceptorProcessor {

	/** slf4j logger */
	private static final Logger logger = LoggerFactory.getLogger(StatusEventProcessor.class);
	/** Servicio de SMS */
	private final SMSService smsService;
	/** Servicio de estaciones */
	private final StationService stationService;
	/** Event service **/
	private final EventStatusService eventService;
	/** Simple Date Format */
	private final String SDF = "yyMMddHH:mm";

	@Inject
	public StatusEventProcessor(SMSService smsService, StationService stationService, EventStatusService eventService) {
		this.smsService = smsService;
		this.stationService = stationService;
		this.eventService = eventService;
	}

	@Override
	public Message process(InboundMessage inboundMessage) {
		logger.debug("Procesando mensaje de Evento de la estacion");
		SMSStatus sms = new SMSStatus(inboundMessage);

		Station station = stationService.getStationBySimNumber(inboundMessage.getOriginator());
		if (station == null) {
			logger.error("The station with SIMCARD # {} does not exist", inboundMessage.getOriginator());
			smsService.updateToFailStatus(sms);
			return sms;
		}

		String[] textSplited = inboundMessage.getText().split(Config.SPLIT_CHARACTER_GENERAL);
		sms.setStation(station);

		Date date = null;
		try {
			date = DateUtils.parseDate(textSplited[1] + textSplited[2], SDF);
		} catch (ParseException e) {
			logger.error("Error parseando fecha ", e);
		}
		sms.setDate(date);

		String code = textSplited[3];
		EventStatus event = eventService.findEventStatusByCode(code);
		if (event == null) {
			logger.error("No se encontro evento con codigo {}", code);
			smsService.updateToFailStatus(sms);
			return sms;
		}
		sms.setEvent(event);

		if (textSplited.length > 5) {
			sms.setValue(textSplited[4]);
		}

		// Si no es critico, lo marcamos como completo, sino lo dejamos en P
		// para que se envien mails
		if (!event.isCritical()) {
			sms.setState(Message.Status.COMPLETED.getCode());
		}
		smsService.postProcessSMS(sms);
		return sms;
	}

}
