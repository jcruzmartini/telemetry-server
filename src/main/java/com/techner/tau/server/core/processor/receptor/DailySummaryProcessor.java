package com.techner.tau.server.core.processor.receptor;

import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smslib.InboundMessage;

import com.google.inject.Inject;
import com.techner.tau.server.core.annotation.AlertCheckerPoolExecutor;
import com.techner.tau.server.core.annotation.HourlyMeasuresPoolExecutor;
import com.techner.tau.server.core.annotation.NotificationsPoolExecutor;
import com.techner.tau.server.core.annotation.TweetsPoolExecutor;
import com.techner.tau.server.core.calculator.alert.AlertCheckerTask;
import com.techner.tau.server.core.calculator.measure.hourly.HourlyMeasureCalculatorTask;
import com.techner.tau.server.core.calculator.notification.NotificationCalculatorTask;
import com.techner.tau.server.core.twitter.TwitterUpdateStatusTask;
import com.techner.tau.server.core.utils.MeasuresFactory;
import com.techner.tau.server.domain.interfaces.Message;
import com.techner.tau.server.domain.model.Measure;
import com.techner.tau.server.domain.model.SMS;
import com.techner.tau.server.domain.model.SMSDailySummary;
import com.techner.tau.server.domain.model.Station;
import com.techner.tau.server.services.MeasureService;
import com.techner.tau.server.services.SMSService;
import com.techner.tau.server.services.StationService;

/**
 * Procesador encargado de la recepcion de medidas diarias tomadas por la
 * estacion
 * 
 * @author juan
 * 
 */
public class DailySummaryProcessor implements ReceptorProcessor {

	/** slf4j logger */
	private static final Logger logger = LoggerFactory.getLogger(DailySummaryProcessor.class);
	/** Servicio de SMS */
	private final SMSService smsService;
	/** Factoria de medidas */
	private final MeasuresFactory measureFactory;
	/** Servicio de medidas */
	private final MeasureService measureService;
	/** Servicio de estacion */
	private final StationService stationService;
	/** Thread Pool Executor */
	private final ThreadPoolExecutor pool;
	/** Thread Pool Executor */
	private final ThreadPoolExecutor poolNotifications;
	/** Thread Pool Executor */
	private final ThreadPoolExecutor tweetsPool;
	/** Factoria para calculo de medidas calculables */
	private final HourlyMeasureCalculatorTask.Factory factory;
	/** Factoria para calculo de medidas calculables */
	private final NotificationCalculatorTask.Factory factoryNotifications;
	/** Factoria para la actualizacion del estado de twitter */
	private final TwitterUpdateStatusTask.Factory factoryTwitterUpdate;
	/** Factoria para la actualizacion del estado de twitter */
	private final AlertCheckerTask.Factory factoryAlertCheck;
	/** Thread Pool Executor */
	private final ThreadPoolExecutor poolAlertCheck;
	/**ID de la estacion que publica en tweeter**/
	private final static int ID_STATION_TWITTER = 6;

	@Inject
	public DailySummaryProcessor(SMSService smsService, MeasuresFactory measureFactory, MeasureService measureService,
			StationService stationService, @HourlyMeasuresPoolExecutor ThreadPoolExecutor pool,
			@NotificationsPoolExecutor ThreadPoolExecutor poolNotifications,
			HourlyMeasureCalculatorTask.Factory factory, NotificationCalculatorTask.Factory factoryNotifications,
			@TweetsPoolExecutor ThreadPoolExecutor tweetsPool, TwitterUpdateStatusTask.Factory factoryTwitterUpdate,
			AlertCheckerTask.Factory factoryAlertCheck, @AlertCheckerPoolExecutor ThreadPoolExecutor poolAlertCheck) {
		this.smsService = smsService;
		this.measureFactory = measureFactory;
		this.measureService = measureService;
		this.stationService = stationService;
		this.pool = pool;
		this.poolNotifications = poolNotifications;
		this.factory = factory;
		this.factoryNotifications = factoryNotifications;
		this.tweetsPool = tweetsPool;
		this.factoryTwitterUpdate = factoryTwitterUpdate;
		this.factoryAlertCheck = factoryAlertCheck;
		this.poolAlertCheck = poolAlertCheck;
	}

	@Override
	public Message process(InboundMessage inboundMessage) {
		logger.debug("Procesando mensaje de medidas diarias programadas");
		SMSDailySummary sms = new SMSDailySummary(inboundMessage);
		String originator = inboundMessage.getOriginator();
		Station station = stationService.getStationBySimNumber(originator);
		if (station == null) {
			logger.error("No existe estacion con simCard # {} ", inboundMessage.getOriginator());
			smsService.updateToFailStatus(sms);
			return sms;
		}

		List<Measure> listMeasure = measureFactory.getListOfMeasures(sms);

		if (!CollectionUtils.isEmpty(listMeasure)) {
			enqueTaks(station, listMeasure);
			measureService.saveMeasures(listMeasure, station);
			sms.setState(SMS.Status.COMPLETED.getCode());
		} else {
			sms.setState(SMS.Status.FAILED.getCode());
			logger.error("Reporte diario de medidas sin medidas que almacenar. Texto SMS: {}", inboundMessage.getText());
		}
		smsService.postProcessSMS(sms);
		return sms;
	}

	/**
	 * Metodo que encola los trabajos de calculos en las diferentes colas
	 * 
	 * @param station
	 *            estacion
	 * @param listMeasure
	 *            lista de medidas
	 */
	private void enqueTaks(Station station, List<Measure> listMeasure) {
		// Encolamos el trabajo de calculo de las variables calculables
		HourlyMeasureCalculatorTask task = factory.create(listMeasure, station);
		pool.submit(task);

		// Encolamos el trabajo de calculo de las notificaciones
		NotificationCalculatorTask taskNoti = factoryNotifications.create(station);
		poolNotifications.submit(taskNoti);

		// Encolamos el trabajo de chequeo de las alertas
		AlertCheckerTask taskAlert = factoryAlertCheck.create(station, listMeasure);
		poolAlertCheck.submit(taskAlert);

		// Publico en twitter TODO: es por ahora.
		if (station.getId() == ID_STATION_TWITTER) {
		    tweetsPool.submit(factoryTwitterUpdate.create(listMeasure, station));
		}
	}

}
