package com.techner.tau.server.core.calculator.measure.daily;

import java.rmi.ServerException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.techner.tau.server.core.calculator.measure.MeasureCalculator;
import com.techner.tau.server.domain.model.Measure;
import com.techner.tau.server.domain.model.Station;
import com.techner.tau.server.services.MeasureService;
import com.techner.tau.server.services.VariableService;

/**
 * Calcula todas las medidas que son calculadas una vez por día para una
 * estacion
 * 
 * @author jmartini
 * 
 */
public class DailyMeasureCalculatorTaskImpl implements DailyMeasureCalculatorTask {
	/** slf4j logger */
	private static final Logger logger = LoggerFactory.getLogger(DailyMeasureCalculatorTaskImpl.class);
	/** calculators para medidas */
	private final Map<String, MeasureCalculator> calculators;
	/** Varibale service */
	private final VariableService variableService;
	/** Id de la estaciòn */
	private final Station station;
	/** Varibale service */
	private final MeasureService measureService;

	@Inject
	public DailyMeasureCalculatorTaskImpl(@Assisted Station station, Map<String, MeasureCalculator> calculators,
			MeasureService measureService, VariableService variableService) {
		this.station = station;
		this.calculators = calculators;
		this.variableService = variableService;
		this.measureService = measureService;
	}

	@Override
	public void run() {
		List<Measure> measures = new ArrayList<Measure>();
		List<String> codes = variableService.getMeteorologicalVariablesEnableByStation(station.getId());
		Set<Entry<String, MeasureCalculator>> entries = calculators.entrySet();

		logger.info("Calculando variables derivadas con periodos diarios para la estacion {}", station.getId());
		for (Entry<String, MeasureCalculator> entry : entries) {
			String code = entry.getKey();
			Measure calculatedMeasure = null;

			if (entry.getValue().getPeriod() == MeasureCalculator.Period.daily && codes.contains(code)) {
				try {
					calculatedMeasure = entry.getValue().calculate(code, station, codes);
				} catch (ServerException e) {
					logger.error(String.format(
							"Error calculando medida de calculo diario con codigo %s para la estación%s ", code,
							station), e);
					continue;
				}
			}
			if (calculatedMeasure != null) {
				measures.add(calculatedMeasure);
			}
		}

		if (!measures.isEmpty()) {
			logger.info("Almacenando {} variables calculadas para la estacion {}", measures.size(), station.getId());
			measureService.saveMeasures(measures, station);
		}
	}
}
