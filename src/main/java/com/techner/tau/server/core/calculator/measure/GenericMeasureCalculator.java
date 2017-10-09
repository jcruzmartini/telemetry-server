package com.techner.tau.server.core.calculator.measure;

import java.util.Date;
import java.util.List;

import com.techner.tau.server.domain.model.Measure;
import com.techner.tau.server.domain.model.Variable;
import com.techner.tau.server.services.VariableService;

public abstract class GenericMeasureCalculator implements MeasureCalculator {

	/** Variable service */
	protected final VariableService variableService;

	public GenericMeasureCalculator(VariableService variableService) {
		this.variableService = variableService;
	}

	/**
	 * Obtiene el valor de una variable determinada
	 * 
	 * @param code
	 *            codigo de la variable
	 * @param measures
	 *            lista de medidas
	 * @return valor de la variable
	 */
	protected Double getMeasureValue(String code, List<Measure> measures) {
		for (Measure measure : measures) {
			if (measure.getVariable().getCode().equals(code)) {
				return measure.getValue();
			}
		}
		return null;
	}

	/**
	 * Obtiene la fecha de las medidas tomadas
	 * 
	 * @param measures
	 *            lista de medidas tomadas
	 * @return fecha
	 */
	protected Date getDateFromMeasures(List<Measure> measures) {
		for (Measure measure : measures) {
			if (measure.getDate() != null) {
				return measure.getDate();
			}
		}
		return new Date();
	}

	/**
	 * Devuelve una nueva medida con la variable indicada
	 * 
	 * @param variableCode
	 *            codigo de variable
	 * @param date
	 *            fecha de la medida
	 * @return medida
	 */
	protected Measure getNewMeasure(String variableCode, Date date) {
		Measure hI = null;
		// Busco la variable DW
		Variable variable = variableService.getVariableByCode(variableCode);
		if (variable != null) {
			hI = new Measure();
			hI.setVariable(variable);
			hI.setDate(date);
			hI.setCalculated(true);
		}
		return hI;
	}
}
