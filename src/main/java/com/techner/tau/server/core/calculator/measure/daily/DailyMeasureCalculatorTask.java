package com.techner.tau.server.core.calculator.measure.daily;

import com.google.inject.assistedinject.Assisted;
import com.techner.tau.server.domain.model.Station;

/***
 * Interfaz de tarea de calculo de medidas que se calculan una sola vez por día
 * 
 * @author juan
 * 
 */
public interface DailyMeasureCalculatorTask extends Runnable {

	/**
	 * Factory para tareas de calculo de variables
	 * 
	 * @author juan
	 * 
	 */
	interface Factory {
		DailyMeasureCalculatorTask create(@Assisted Station station);
	}

}
