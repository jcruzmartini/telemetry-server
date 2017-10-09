package com.techner.tau.server.core.calculator.notification;

import com.google.inject.assistedinject.Assisted;
import com.techner.tau.server.domain.model.Station;

/***
 * Interfaz de tarea de calculo de alertas
 * 
 * @author juan
 * 
 */
public interface NotificationCalculatorTask extends Runnable {

	/**
	 * Factory para tareas de calculo de variables
	 * 
	 * @author juan
	 * 
	 */
	interface Factory {
		NotificationCalculatorTask create(@Assisted Station station);
	}

}
