package com.techner.tau.server.core.processor.receptor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smslib.InboundMessage;

import com.google.inject.Inject;
import com.techner.tau.server.core.annotation.HourlyMeasuresPoolExecutor;
import com.techner.tau.server.core.calculator.measure.MeasureCalculator;
import com.techner.tau.server.core.calculator.measure.hourly.HourlyMeasureCalculatorTask;
import com.techner.tau.server.core.config.Config;
import com.techner.tau.server.core.utils.MeasuresFactory;
import com.techner.tau.server.domain.interfaces.Message;
import com.techner.tau.server.domain.model.Measure;
import com.techner.tau.server.domain.model.SMS;
import com.techner.tau.server.domain.model.SMSCustomerRequest;
import com.techner.tau.server.domain.model.SMSResponse;
import com.techner.tau.server.domain.model.SMSServerOperation;
import com.techner.tau.server.domain.model.Station;
import com.techner.tau.server.services.MeasureService;
import com.techner.tau.server.services.SMSService;
import com.techner.tau.server.services.StationService;

/**
 * Procesador encargado de la recepcion de una respuesta de la estacion a un
 * pedido de datos por parte del cliente
 * 
 * @author juan
 * 
 */
public class ResponseProcessor implements ReceptorProcessor {
	/** slf4j logger */
	private static final Logger logger = LoggerFactory.getLogger(ResponseProcessor.class);
	/** Servicio de SMS */
	private final SMSService smsService;
	/** Factoria de medidas */
	private final MeasuresFactory measureFactory;
	/** Servicio de estaciones */
	private final StationService stationService;
	/** Servicio de medidas */
	private final MeasureService measureService;
	/** Config */
	private final List<String> excludedSaverVariables;
	/** Thread Pool Executor */
	private final ThreadPoolExecutor pool;
	/** Factoria para calculo de medidas calculables */
	private final HourlyMeasureCalculatorTask.Factory factory;

	@Inject
	public ResponseProcessor(SMSService smsService, MeasuresFactory measureFactory, StationService stationService,
			MeasureService measureService, Config config, @HourlyMeasuresPoolExecutor ThreadPoolExecutor pool,
			HourlyMeasureCalculatorTask.Factory factory) {
		this.smsService = smsService;
		this.measureFactory = measureFactory;
		this.stationService = stationService;
		this.measureService = measureService;
		this.excludedSaverVariables = Arrays.asList(config.getExcludedVariablesToSave());
		this.pool = pool;
		this.factory = factory;
	}

	@Override
	public Message process(InboundMessage inboundMessage) {
		// La demanda pudo haber sido ocasionada por un cliente via SMS o por
		// una operaciòn via WEB
		logger.info("Procesando respuesta de consulta por demanda");
		String originator = inboundMessage.getOriginator();
		SMS sms = new SMS(inboundMessage);

		Station station = stationService.getStationBySimNumber(originator);
		if (station == null) {
			logger.error("Estación con simCard # {} no existe", originator);
			smsService.updateToFailStatus(sms);
			return sms;
		}

		Integer token = smsService.getTokenNumber(inboundMessage.getText());

		// Obtengo lista de medidas
		List<Measure> listMeasure = measureFactory.getListOfMeasures(sms);

		// Remplazo medida actual por acumulado
		Double acumPrecipitation = measureService.getPrecipitationAcum(MeasureCalculator.PRECIPITATION_CODE,
				station.getId());
		measureService.replaceActualPrecipitationForAcum(listMeasure, acumPrecipitation);

		// Filtro las medidas que no son almacenables
		List<Measure> listMeasureFiltered = filterNotSaverMeasures(listMeasure);

		// Si es una consulta enviada por un usuario deberìa haber un customer
		// request pendiente
		SMSCustomerRequest pendingCusomerRequestSMS = smsService.getSMSCustomerRequestWaitingForResponse(
				station.getId(), token);
		SMSServerOperation pendingOperationSMS = null;
		if (pendingCusomerRequestSMS == null) {
			// Sino, chequeamos si es una operacion pendiente
			pendingOperationSMS = smsService.getSMSOperationWaitingForResponse(originator, token);
			if (pendingOperationSMS != null) {
				if (!CollectionUtils.isEmpty(listMeasure)) {
					pendingOperationSMS.setSuccess(Boolean.TRUE);
				} else {
					pendingOperationSMS.setSuccess(Boolean.FALSE);
				}

				sms.setState(SMS.Status.COMPLETED.getCode());
				smsService.saveOrUpdateSMS(sms);

				pendingOperationSMS.setDelivered_at(new Date());
				pendingOperationSMS.setState(SMS.Status.COMPLETED.getCode());

				smsService.saveOrUpdateSMS(pendingOperationSMS);
				sms = pendingOperationSMS;
			} else {
				logger.error("Respuesta por consulta por demanda, sin Customer Request ni operacion en estado: {} ",
						SMS.Status.WAITING_STATION_RESPONSE.name());
				smsService.updateToFailStatus(sms);
				return sms;
			}
		} else {
			SMSResponse smsResp = new SMSResponse(inboundMessage);
			smsResp.setTo(pendingCusomerRequestSMS.getFrom());

			String friendlyText = smsService.buildFriendlyText(listMeasure);
			smsResp.setFriendlyText(friendlyText);

			// Necesito grabar antes para usar el bean
			smsService.saveOrUpdateSMS(smsResp);

			pendingCusomerRequestSMS.setState(SMS.Status.COMPLETED.getCode());
			pendingCusomerRequestSMS.setResponse(smsResp);
			pendingCusomerRequestSMS.setDelivered_at(new Date());
			smsService.saveOrUpdateSMS(pendingCusomerRequestSMS);
			sms = smsResp;
		}

		// Encolamos el trabajo de calculo de las variables calculables
		HourlyMeasureCalculatorTask task = factory.create(listMeasure, station);
		pool.submit(task);
		// Guardo las medidas en la base de datos
		measureService.saveMeasures(listMeasureFiltered, station);
		return sms;
	}

	/**
	 * Saca las medidas que nos son almacenables en la consulta por demanda o
	 * por operacion a la estacion Por ej. Precipitaciòn
	 * 
	 * @param listMeasure
	 *            lista de medidas
	 * @return
	 */
	private List<Measure> filterNotSaverMeasures(List<Measure> listMeasure) {
		List<Measure> filtered = new ArrayList<Measure>();
		for (Measure measure : listMeasure) {
			if (!excludedSaverVariables.contains(measure.getVariable().getCode())) {
				filtered.add(measure);
			}
		}
		return filtered;
	}

}
