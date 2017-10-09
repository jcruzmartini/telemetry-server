package com.techner.tau.server.core.calculator.measure.hourly;

import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.techner.tau.server.core.calculator.measure.GenericMeasureCalculator;
import com.techner.tau.server.core.calculator.measure.MeasureCalculator;
import com.techner.tau.server.domain.model.Measure;
import com.techner.tau.server.domain.model.Station;
import com.techner.tau.server.services.VariableService;

public class DewPointCalculator extends GenericMeasureCalculator {
	/** slf4j logger */
	private static final Logger logger = LoggerFactory.getLogger(DewPointCalculator.class);
	/** Codigo de la variable a calculatr */
	private String variableCode;
	/** Fecha de la medida */
	private Date date;

	@Inject
	public DewPointCalculator(VariableService variableService) {
		super(variableService);
	}

	@Override
	public Measure calculate(String variable, List<Measure> measures, List<String> enablesVars) {
		if (enablesVars != null) {
			if (!enablesVars.contains(TEMPERATURE_CODE) || !enablesVars.contains(HUMIDITY_CODE)) {
				logger.warn("Escapando el calculo de ITH. Variables [{}] [{}] necesarias, no habilitadas",
						TEMPERATURE_CODE, HUMIDITY_CODE);
				return null;
			}
		}
		this.variableCode = variable;
		this.date = getDateFromMeasures(measures);
		Double temp = getMeasureValue(TEMPERATURE_CODE, measures);
		Double hum = getMeasureValue(HUMIDITY_CODE, measures);

		if (temp == null || hum == null) {
			return null;

		}
		logger.info("Calculando la variable : {}", variableCode);
		return calculateDewPoint(temp, hum);
	}

	/***
	 * dP Calculate dew point
	 * 
	 * @param temp
	 *            temperatura
	 * @param hum
	 *            humedad
	 * @return dew point calculado
	 */
	private Measure calculateDewPoint(Double temp, Double hum) {
		Double Tn, m;

		Measure dP = getNewMeasure(variableCode, date);
		if (dP == null) {
			return dP;
		}

		Double H = ((Math.log10(hum) - 2.0d) / 0.4343d + (17.62d * temp) / (243.12d + temp));
		if (temp < 0) {
			Tn = 272.62d;
			m = 22.46d;
		} else {
			Tn = 243.12d;
			m = 17.62d;
		}

		Double dPF = (Tn * H / (m - H));
		String formated = String.format(Locale.US, "%.2f%n", dPF);
		dP.setValue(Double.valueOf(formated));

		return dP;
	}

	@Override
	public Period getPeriod() {
		return MeasureCalculator.Period.hourly;
	}

	@Override
	public Measure calculate(String variable, Station station, List<String> enableVars) {
		throw new UnsupportedOperationException("Metodo no implementado para " + this.getClass());
	}
}
