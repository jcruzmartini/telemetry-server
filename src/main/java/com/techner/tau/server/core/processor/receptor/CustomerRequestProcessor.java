package com.techner.tau.server.core.processor.receptor;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smslib.InboundMessage;

import com.google.inject.Inject;
import com.techner.tau.server.domain.interfaces.Message;
import com.techner.tau.server.domain.model.Customer;
import com.techner.tau.server.domain.model.SMSCustomerRequest;
import com.techner.tau.server.domain.model.Station;
import com.techner.tau.server.services.CustomerService;
import com.techner.tau.server.services.MeasureService;
import com.techner.tau.server.services.SMSService;

/**
 * Procesador encargado de la recepcion de peticiones de informacion enviadas
 * por el cliente
 * 
 * @author juan
 * 
 */
public class CustomerRequestProcessor implements ReceptorProcessor {

	/** slf4j logger */
	private static final Logger logger = LoggerFactory.getLogger(AlertStationProcessor.class);
	/** Servicio de SMS */
	private final SMSService smsService;
	/** Servicio de Clientes */
	private final CustomerService customerService;

	@Inject
	public CustomerRequestProcessor(SMSService smsService, CustomerService customerService,
			MeasureService measureService) {
		this.smsService = smsService;
		this.customerService = customerService;
	}

	@Override
	public Message process(InboundMessage inboundMessage) {
		logger.info("Procesando consulta por demanda del usuario");
		SMSCustomerRequest sms = new SMSCustomerRequest(inboundMessage);

		Customer customer = customerService.getCustomeBySimCard(inboundMessage.getOriginator());
		if (customer == null) {
			logger.error("The SIMCARD # {} does not have any customer associated", inboundMessage.getOriginator());
			smsService.updateToFailStatus(sms);
			return sms;
		}

		if (CollectionUtils.isEmpty(customer.getStations())) {
			logger.error("El cliente ID # {} no tiene ninguna estacion meteorologica asociada", customer.getId());
			smsService.updateToFailStatus(sms);
			return sms;
		}

		sms.setCustomer(customer);
		// TODO: multi estaciones aqui
		Station station = customer.getStations().get(0);
		sms.setStation(station);

		smsService.postProcessSMS(sms, true);
		return sms;
	}

}
