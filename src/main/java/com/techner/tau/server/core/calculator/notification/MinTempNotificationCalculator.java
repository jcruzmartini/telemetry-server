package com.techner.tau.server.core.calculator.notification;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.techner.tau.server.core.calculator.measure.MeasureCalculator;
import com.techner.tau.server.domain.model.EventNotification;
import com.techner.tau.server.domain.model.Measure;
import com.techner.tau.server.domain.model.Station;
import com.techner.tau.server.domain.model.Variable;
import com.techner.tau.server.services.EventNotificationService;
import com.techner.tau.server.services.MeasureService;
import com.techner.tau.server.services.NotificationService;
import com.techner.tau.server.services.RiskDescriptionCommentService;
import com.techner.tau.server.services.StationService;
import com.techner.tau.server.services.VariableService;

/**
 * Calcula el riesgo de heladas y la temperatura minima del dia siguiente
 * 
 * @author juan
 * 
 */
public class MinTempNotificationCalculator extends GenericNotificationCalculator {
	/** logger **/
	private static final Logger logger = LoggerFactory.getLogger(MinTempNotificationCalculator.class);
	/** codigo de notificacion **/
	private String codNotification;
	/** estacion **/
	private Station station;
	/** servicio estacion **/
	private final StationService stationService;

	@Inject
	public MinTempNotificationCalculator(VariableService variableService, NotificationService notificationService,
			MeasureService measureService, RiskDescriptionCommentService commentService,
			EventNotificationService eventNotificationService, StationService stationService) {
		super(variableService, notificationService, measureService, commentService, eventNotificationService);
		this.stationService = stationService;
	}

	@Override
	public List<EventNotification> calculate(String codNotificacion, Station station) {
		this.codNotification = codNotificacion;
		this.station = station;
		List<EventNotification> notifications = null;
		Integer id = station.getId();

		Date now = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(now);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		Date date = cal.getTime();
		Date sunset = stationService.getSunsetTime(station, date);
		if (sunset == null) {
			logger.warn("No hay informacion de la puesta del sol para la estacion {} y la fecha {}", id, date);
			return null;
		}
		cal.setTime(sunset);
		// Minus 2 hours to sunset time
		cal.add(Calendar.HOUR_OF_DAY, -2);
		Date sunsetMinusTwo = cal.getTime();

		long timeMillSunset = sunsetMinusTwo.getTime();
		long timeMillNow = now.getTime();

		if (timeMillNow < timeMillSunset) {
			return null;
		}
		Measure lastMinTemp = measureService.getLastMeasureValue(MIN_TEMPERTATURE_CODE, id);

		if (lastMinTemp != null) {
			cal.setTime(now);
			Calendar minTempCal = Calendar.getInstance();
			minTempCal.setTime(lastMinTemp.getDate());

			int nowDay = cal.get(Calendar.DAY_OF_MONTH);
			int minDay = minTempCal.get(Calendar.DAY_OF_MONTH);
			// Si la medida es de hoy, no calculo de nuevo
			if (nowDay != minDay) {
				notifications = calculate(sunsetMinusTwo);
			}
		} else {
			notifications = calculate(sunsetMinusTwo);
		}

		return notifications;
	}

	private List<EventNotification> calculate(Date sunsetMinusTwo) {
		Double temp = measureService.getMeasureaAtTime(MeasureCalculator.TEMPERATURE_CODE, sunsetMinusTwo,
				station.getId());
		Double dw = measureService.getMeasureaAtTime(MeasureCalculator.DEW_POINT_CODE, sunsetMinusTwo, station.getId());

		if (temp == null || dw == null) {
			logger.error("Valor de temperatura o punto de rocio no puede ser nulos. Temp: {} . P. Rocio: {}", temp, dw);
			return null;
		}
		logger.info("Evaluando la notificacion : {}", codNotification);
		return calculateMinTemp(temp, dw);
	}

	/**
	 * Calcula el evento de Heladas
	 * 
	 * @param temp
	 *            temperatura
	 * @param hum
	 *            humedad
	 * @return lista de eventos
	 */
	private List<EventNotification> calculateMinTemp(Double temp, Double dw) {
		List<EventNotification> notifications = new ArrayList<EventNotification>();
		Double tp = (0.573 * temp) + (0.305 * dw) - 4.621d;
		logger.info(String
				.format("Calculando TEMPERATURA MINIMA con temperatura = %s y punto de rocio = %s. Resultado Temp. Minima = %s",
						temp, dw, tp));
		String formatedTp = String.format(Locale.US, "%.2f%n", tp);
		Variable variable = variableService.getVariableByCode(MIN_TEMPERTATURE_CODE);
		if (variable == null) {
			logger.error("No se pudo encontrar variable con codigo {}", MIN_TEMPERTATURE_CODE);
			return null;
		}

		tp = Double.valueOf(formatedTp);
		Date truncatedDate = DateUtils.truncate(new Date(), Calendar.DATE);
		Measure measure = new Measure(variable, tp, truncatedDate, station, true);
		measureService.saveOrUpdate(measure);

		String criticality = null;
		if (tp > 0 && tp <= 5) {
			criticality = NotificationsCalculator.Criticality.MED.getCode();
		} else if (tp <= 0) {
			criticality = NotificationsCalculator.Criticality.HIGH.getCode();
		}

		EventNotification event = getEventNotification(codNotification, criticality, station);
		if (criticality != null) {
			if (event != null) {
				event.setValue(tp);
				notifications.add(event);
			}
		} else {
			logger.debug("No hay riesgos de heladas para esta fecha ");
		}
		return notifications;
	}
}
