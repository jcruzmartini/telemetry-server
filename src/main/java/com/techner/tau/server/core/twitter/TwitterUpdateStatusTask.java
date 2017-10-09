package com.techner.tau.server.core.twitter;

import java.util.List;

import com.google.inject.assistedinject.Assisted;
import com.techner.tau.server.domain.model.Measure;
import com.techner.tau.server.domain.model.Station;

/***
 * Interfaz de tarea de calculo de alertas
 * 
 * @author juan
 * 
 */
public interface TwitterUpdateStatusTask extends Runnable {

	/**
	 * Factory para tareas de calculo de variables
	 * 
	 * @author juan
	 * 
	 */
	interface Factory {
		/**
		 * Create tsak
		 * 
		 * @param measures
		 *            medidas
		 * @param station
		 *            estacion
		 * @return publisher task
		 */
		TwitterUpdateStatusTask create(@Assisted List<Measure> measures, @Assisted Station station);
	}

}
