package com.techner.tau.server.core.calculator.measure.hourly;

import java.util.List;

import com.google.inject.assistedinject.Assisted;
import com.techner.tau.server.domain.model.Measure;
import com.techner.tau.server.domain.model.Station;

/***
 * Interfaz de tarea de calculo de medidas que se calculan cada una hora
 * 
 * @author juan
 * 
 */
public interface HourlyMeasureCalculatorTask extends Runnable {

	/**
	 * Factory para tareas de calculo de variables
	 * 
	 * @author juan
	 * 
	 */
	interface Factory {
		HourlyMeasureCalculatorTask create(@Assisted List<Measure> listMeasure, @Assisted Station station);
	}

}
