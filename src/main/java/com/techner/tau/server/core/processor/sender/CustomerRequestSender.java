package com.techner.tau.server.core.processor.sender;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smslib.OutboundMessage;

import com.google.inject.Inject;
import com.techner.tau.server.core.calculator.measure.MeasureCalculator;
import com.techner.tau.server.domain.model.Measure;
import com.techner.tau.server.domain.model.SMS;
import com.techner.tau.server.domain.model.SMSCustomerRequest;
import com.techner.tau.server.domain.model.SimCard;
import com.techner.tau.server.services.MeasureService;
import com.techner.tau.server.services.SMSService;

/**
 * Procesador de envio de mensajes de respuesta a una solicitud de datos
 * enviadas por el cliente
 * 
 * @author juan
 * 
 */
public class CustomerRequestSender implements SenderProcessor<SMSCustomerRequest> {

	/** slf4j logger. */
	private static final Logger logger = LoggerFactory.getLogger(CustomerRequestSender.class);
	/** Servicio de sms */
	private final SMSService smsService;
	/** Servicio de medidas **/
	private final MeasureService measureService;

	@Inject
	public CustomerRequestSender(SMSService smsService, MeasureService measureService) {
		this.smsService = smsService;
		this.measureService = measureService;
	}

	@Override
	public List<OutboundMessage> process(SMSCustomerRequest sms) {
		List<OutboundMessage> listSmsToSend = new ArrayList<OutboundMessage>();
		logger.debug("Processing to Send -  CUSTOMER_REQUEST message");

		if (!StringUtils.isBlank(sms.getText())) {
			List<Measure> measures = measureService.getLastMeasures(sms.getStation().getId(), 15);
			if (CollectionUtils.isNotEmpty(measures)) {
				Double acumPrecipitation = measureService.getPrecipitationAcum(MeasureCalculator.PRECIPITATION_CODE,
						sms.getStation().getId());
				// replace actual for acum
				measureService.replaceActualPrecipitationForAcum(measures, acumPrecipitation);

				String text = smsService.buildFriendlyText(measures);
				List<SimCard> simCards = sms.getStation().getCustomer().getSimCards();
				for (SimCard simCard : simCards) {
					listSmsToSend.add(smsService.createMessageToSend(text, simCard.getNumber()));
				}
				sms.setState(SMS.Status.COMPLETED.getCode());
				logger.info("Respondiendo consulta de usuario con medidas tomadas recientemente");
			} else {
				listSmsToSend.add(smsService.createMessageToSend(sms.getText(), sms.getStation().getSimCard()
						.getNumber()));
				sms.setState(SMS.Status.WAITING_STATION_RESPONSE.getCode());
			}
		} else {
			logger.error("Error processing customer request sms . Query station text is NULL. SMS # {} set to {} ",
					sms.getId(), SMS.Status.FAILED.getCode());
			sms.setState(SMS.Status.FAILED.getCode());
		}
		return listSmsToSend;
	}

}
