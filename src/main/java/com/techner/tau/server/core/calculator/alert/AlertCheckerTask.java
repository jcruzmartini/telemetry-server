package com.techner.tau.server.core.calculator.alert;

import java.util.List;

import com.google.inject.assistedinject.Assisted;
import com.techner.tau.server.domain.model.Measure;
import com.techner.tau.server.domain.model.Station;

/***
 * Interfaz de tarea de chequeo de alertas
 * 
 * @author juan
 * 
 */
public interface AlertCheckerTask extends Runnable {

	/**
	 * Factory para tareas de chequeo de alertas
	 * 
	 * @author juan
	 * 
	 */
	interface Factory {
		AlertCheckerTask create(@Assisted Station station,@Assisted List<Measure> listMeasures );
	}

}
