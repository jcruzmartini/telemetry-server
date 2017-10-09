package com.techner.tau.server.core.calculator.measure.hourly;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ThreadPoolExecutor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.techner.tau.server.core.annotation.AlertCheckerPoolExecutor;
import com.techner.tau.server.core.annotation.TweetsPoolExecutor;
import com.techner.tau.server.core.calculator.alert.AlertCheckerTask;
import com.techner.tau.server.core.calculator.measure.MeasureCalculator;
import com.techner.tau.server.core.twitter.TwitterUpdateStatusTask;
import com.techner.tau.server.domain.model.Measure;
import com.techner.tau.server.domain.model.Station;
import com.techner.tau.server.services.MeasureService;
import com.techner.tau.server.services.VariableService;

/**
 * Calculador de medidas deducibles de otras
 * 
 * @author jmartini
 * 
 */
public class HourlyMeasureCalculatorTaskImpl implements HourlyMeasureCalculatorTask {
	/** slf4j logger */
	private static final Logger logger = LoggerFactory.getLogger(HourlyMeasureCalculatorTaskImpl.class);
	/** Id de la estacion a calcular la alerta **/
	private final List<Measure> listMeasure;
	/** calculators para medidas */
	private final Map<String, MeasureCalculator> calculators;
	/** Varibale service */
	private final MeasureService measureService;
	/** Varibale service */
	private final VariableService variableService;
	/** Id de la estaciòn */
	private final Station station;
	/** Factoria para la actualizacion del estado de twitter */
	private final TwitterUpdateStatusTask.Factory factoryTwitterUpdate;
	/** Thread Pool Executor */
	private final ThreadPoolExecutor tweetsPool;
	/** Thread Pool Executor for alarms check */
	private final ThreadPoolExecutor alertCheckPool;
	/** Factoria para chequeo de alertas */
	private final AlertCheckerTask.Factory factoryAlertCheck;

	@Inject
	public HourlyMeasureCalculatorTaskImpl(@Assisted List<Measure> listMeasure, @Assisted Station station,
			Map<String, MeasureCalculator> calculators, MeasureService measureService,
			TwitterUpdateStatusTask.Factory factoryTwitterUpdate, @TweetsPoolExecutor ThreadPoolExecutor tweetsPool,
			VariableService variableService, AlertCheckerTask.Factory factoryAlertCheck,
			@AlertCheckerPoolExecutor ThreadPoolExecutor poolAlertCheck) {
		this.listMeasure = listMeasure;
		this.station = station;
		this.calculators = calculators;
		this.measureService = measureService;
		this.factoryTwitterUpdate = factoryTwitterUpdate;
		this.tweetsPool = tweetsPool;
		this.variableService = variableService;
		this.alertCheckPool = poolAlertCheck;
		this.factoryAlertCheck = factoryAlertCheck;
	}

	@Override
	public void run() {
		List<Measure> measures = new ArrayList<Measure>();
		List<String> codes = variableService.getMeteorologicalVariablesEnableByStation(station.getId());
		Set<Entry<String, MeasureCalculator>> entries = calculators.entrySet();

		for (Entry<String, MeasureCalculator> entry : entries) {
			String code = entry.getKey();
			Measure calculatedMeasure = null;
			if (entry.getValue().getPeriod() == MeasureCalculator.Period.hourly && codes.contains(code)) {
				calculatedMeasure = entry.getValue().calculate(code, listMeasure, codes);
			}
			if (calculatedMeasure != null) {
				measures.add(calculatedMeasure);
			}
		}
		if (!measures.isEmpty()) {
			logger.info("Almacenando {} medidas calculadas para la estacion {}", measures.size(), station.getId());
			// encolamos trabajos a realizar
			tweetsPool.submit(factoryTwitterUpdate.create(measures, station));
			alertCheckPool.submit(factoryAlertCheck.create(station, measures));
			// guardamos medidas
			measureService.saveMeasures(measures, station);
		}
	}
}
