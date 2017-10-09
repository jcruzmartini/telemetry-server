package com.techner.tau.server.core.calculator.measure;

import java.rmi.ServerException;
import java.util.List;

import com.techner.tau.server.core.calculator.measure.daily.EvapotranspirationCalculator;
import com.techner.tau.server.core.calculator.measure.hourly.DewPointCalculator;
import com.techner.tau.server.core.calculator.measure.hourly.HeatIndexCalculator;
import com.techner.tau.server.core.calculator.measure.hourly.ITHCalculator;
import com.techner.tau.server.domain.model.Measure;
import com.techner.tau.server.domain.model.Station;

/**
 * Interfaz a ser implementada por todos los calculadores de variables
 * calculables
 * 
 * @author juan
 * 
 */
public interface MeasureCalculator {
	/** Codigo para temperatura */
	String TEMPERATURE_CODE = "T";
	/** Codigo para la lluvia */
	String PRECIPITATION_CODE = "L";
	/** Codigo para humedad */
	String HUMIDITY_CODE = "H";
	/** Codigo para humedad */
	String PRESURE_CODE = "P";
	/** Codigo para humedad */
	String RADIATION_CODE = "R";
	/** Codigo para humedad */
	String WIND_VELOCITY_CODE = "V";
	/** Codigo para dew point */
	String DEW_POINT_CODE = "DP";
	/** Codigo para heat index */
	String HEAT_INDEX_CODE = "HI";
	/** Codigo para indice temperatura y humedad */
	String ITH_CODE = "ITH";
	/** Codigo para indice temperatura y humedad */
	String EVAP_CODE = "E";

	enum Calculators {

		DEW_POINT(DEW_POINT_CODE, DewPointCalculator.class), HEAT_INDEX(HEAT_INDEX_CODE, HeatIndexCalculator.class), ITH(
				ITH_CODE, ITHCalculator.class), EVAP(EVAP_CODE, EvapotranspirationCalculator.class);

		/***
		 * Codigo de la variable
		 */
		private String variable;
		/**
		 * Calculador
		 */
		private Class<? extends MeasureCalculator> calculator;

		private Calculators(String variable, Class<? extends MeasureCalculator> calculator) {
			this.variable = variable;
			this.calculator = calculator;
		}

		/**
		 * @return the variable
		 */
		public String getVariable() {
			return variable;
		}

		/**
		 * @return the calculator
		 */
		public Class<? extends MeasureCalculator> getCalculator() {
			return calculator;
		}
	}

	enum Period {
		daily, hourly;
	}

	/**
	 * Calcula el valor de una determinada variable calculable
	 * 
	 * @param measures
	 *            lista de medidas no calculables
	 * @return medida calculada
	 */
	Measure calculate(String variable, List<Measure> measures, List<String> enableVars);

	/**
	 * Calcula el valor de una variable utilizando información de la estación
	 * 
	 * @param varibale
	 *            codigo de la variable a calcular
	 * @param station
	 *            informacion de la estación
	 * @param enableVars
	 *            variables habilitadas
	 * @return medida calculada
	 * @throws ServerException
	 */
	Measure calculate(String variable, Station station, List<String> enableVars) throws ServerException;

	/**
	 * Determina le periodo cada cuanto se calcula
	 * 
	 * @return the period
	 */
	public Period getPeriod();
}
