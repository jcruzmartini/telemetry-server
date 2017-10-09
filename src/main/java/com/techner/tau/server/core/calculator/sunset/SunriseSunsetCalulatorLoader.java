package com.techner.tau.server.core.calculator.sunset;

public interface SunriseSunsetCalulatorLoader extends Runnable {

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
		SunriseSunsetCalulatorLoader create();
	}
}
