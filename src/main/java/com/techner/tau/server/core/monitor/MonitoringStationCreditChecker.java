package com.techner.tau.server.core.monitor;

public interface MonitoringStationCreditChecker extends Runnable {

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
		MonitoringStationCreditChecker create();
	}
}
