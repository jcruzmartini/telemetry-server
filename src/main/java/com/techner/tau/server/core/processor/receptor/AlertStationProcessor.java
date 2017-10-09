package com.techner.tau.server.core.processor.receptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smslib.InboundMessage;

import com.google.inject.Inject;
import com.techner.tau.server.core.config.Config;
import com.techner.tau.server.domain.interfaces.Message;
import com.techner.tau.server.domain.model.Customer;
import com.techner.tau.server.domain.model.SMSStationEvent;
import com.techner.tau.server.domain.model.Station;
import com.techner.tau.server.services.CustomerService;
import com.techner.tau.server.services.SMSService;
import com.techner.tau.server.services.StationService;

/**
 * Procesador encargado de la recepcion de Alertas enviadas por la estacion
 * 
 * @author juan
 * 
 */
public class AlertStationProcessor implements ReceptorProcessor {

	/** slf4j logger */
	private static final Logger logger = LoggerFactory.getLogger(AlertStationProcessor.class);
	/** Servicio de SMS */
	private final SMSService smsService;
	/** Servicio de Clientes */
	private final CustomerService customerService;
	/** Servicio de las estaciones */
	private final StationService stationService;

	@Inject
	public AlertStationProcessor(SMSService smsService, CustomerService customerService, StationService stationService,
			Config config) {
		this.smsService = smsService;
		this.customerService = customerService;
		this.stationService = stationService;
	}

	@Override
	public Message process(InboundMessage inboundMessage) {
		logger.info("Processing ALERT_STATION_EVENT message");
		SMSStationEvent sms = new SMSStationEvent(inboundMessage);

		Customer customer = customerService.getCustomerByStation(inboundMessage.getOriginator());
		if (customer == null) {
			logger.error("The SIMCARD # {} does not have any customer associated", inboundMessage.getOriginator());
			smsService.updateToFailStatus(sms);
			return sms;
		}

		Station station = stationService.getStationBySimNumber(inboundMessage.getOriginator());
		if (station == null) {
			logger.error("Station with simNumber #: {} does not exist", inboundMessage.getOriginator());
			smsService.updateToFailStatus(sms);
			return sms;
		}

		String[] textSplited = inboundMessage.getText().split(Config.SPLIT_CHARACTER_GENERAL);
		sms.setStation(station);
		sms.setCustomer(customer);
		sms.setAlertCode(textSplited[1]);
		sms.setValue(textSplited[2]);
		sms.setConfirmationId(textSplited[3]);

		smsService.postProcessSMS(sms);
		return sms;
	}

}
