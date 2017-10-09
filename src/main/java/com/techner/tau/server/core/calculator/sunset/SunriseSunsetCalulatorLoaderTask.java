package com.techner.tau.server.core.calculator.sunset;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.techner.tau.server.domain.model.AditionalInformation;
import com.techner.tau.server.domain.model.Station;
import com.techner.tau.server.domain.model.SunriseSunsetInformation;
import com.techner.tau.server.services.StationService;

public class SunriseSunsetCalulatorLoaderTask implements SunriseSunsetCalulatorLoader {
	/** logger */
	private static final Logger logger = LoggerFactory.getLogger(SunriseSunsetCalulatorLoaderTask.class);
	private final StationService service;

	@Inject
	public SunriseSunsetCalulatorLoaderTask(StationService service) {
		this.service = service;
	}

	@Override
	public void run() {
		logger.info("Calculando hora de salida y puesta del sol para todas las estaciones. Fecha y Hora : {} ",
				new Date());
		Calendar beginCalendar = getBeginCalendar();

		List<Station> stations = service.getListOfStationWithoutSunsetSunriseInformation(beginCalendar.getTime());

		logger.info("Se encontraron {} estaciones sin información de salida y puesta del sol", stations.size());
		for (Station station : stations) {
			Set<SunriseSunsetInformation> sunriseSunsetInfo = new HashSet<SunriseSunsetInformation>();
			AditionalInformation information = station.getInformation();

			beginCalendar = getBeginCalendar();
			Calendar endCalendar = getEndCalendar(beginCalendar.getTime());

			while (beginCalendar.compareTo(endCalendar) <= 0) {
				SunriseSunsetCalculator calculator = new SunriseSunsetCalculator(information.getLatitude(),
						information.getLongitude(), beginCalendar.getTime(), 0);
				calculator.doCalculations();
				SunriseSunsetInformation info = new SunriseSunsetInformation(information, calculator.getSunset(),
						calculator.getSunrise(), beginCalendar.getTime());
				sunriseSunsetInfo.add(info);
				beginCalendar.add(Calendar.DATE, 1);
			}
			information.setSunriseSunsetInfo(sunriseSunsetInfo);
			station.setInformation(information);
			service.saveOrUpdateStation(station);
		}
	}

	/**
	 * Create begin calendar
	 * 
	 * @return calendar
	 */
	private Calendar getBeginCalendar() {
		Calendar beginCalendar = Calendar.getInstance();
		beginCalendar.setTime(new Date());
		return beginCalendar;
	}

	/***
	 * Create end calendar
	 * 
	 * @param startDate
	 * @return calenadr
	 */
	private Calendar getEndCalendar(Date startDate) {
		Calendar endCalendar = Calendar.getInstance();
		endCalendar.setTime(startDate);
		endCalendar.add(Calendar.DATE, 30);
		return endCalendar;
	}

}
