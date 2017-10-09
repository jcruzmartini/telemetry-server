package com.techner.tau.server.core.calculator.measure.daily.loader;

public interface DailyCalculatedMeasureLoader extends Runnable {

	/**
	 * Factory
	 * 
	 * @author juan
	 * 
	 */
	interface Factory {
		/**
		 * Create task
		 * 
		 * @return task
		 */
		DailyCalculatedMeasureLoader create();
	}
}
