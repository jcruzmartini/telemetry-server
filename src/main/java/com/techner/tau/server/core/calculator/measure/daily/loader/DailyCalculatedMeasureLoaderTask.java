package com.techner.tau.server.core.calculator.measure.daily.loader;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.techner.tau.server.core.annotation.DailyMeasuresPoolExecutor;
import com.techner.tau.server.core.calculator.measure.daily.DailyMeasureCalculatorTask;
import com.techner.tau.server.domain.model.Station;
import com.techner.tau.server.services.StationService;

public class DailyCalculatedMeasureLoaderTask implements DailyCalculatedMeasureLoader {

	/** slf4j logger */
	private static final Logger logger = LoggerFactory.getLogger(DailyCalculatedMeasureLoaderTask.class);
	private final StationService service;
	private final DailyMeasureCalculatorTask.Factory factory;
	/** Thread Pool Executor */
	private final ThreadPoolExecutor pool;

	@Inject
	public DailyCalculatedMeasureLoaderTask(StationService service, DailyMeasureCalculatorTask.Factory factory,
			@DailyMeasuresPoolExecutor ThreadPoolExecutor pool) {
		this.service = service;
		this.factory = factory;
		this.pool = pool;
	}

	@Override
	public void run() {
		logger.info(
				"Calculando variable derivadas con periocidad diarias para todas las estaciones. Fecha y Hora : {} ",
				new Date());
		List<Station> stations = service.getAllEnableStations();
		for (Station station : stations) {
			DailyMeasureCalculatorTask task = factory.create(station);
			pool.submit(task);
		}
	}
}
