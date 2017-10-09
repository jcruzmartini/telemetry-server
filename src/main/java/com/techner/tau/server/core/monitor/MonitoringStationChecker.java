package com.techner.tau.server.core.monitor;

public interface MonitoringStationChecker extends Runnable {

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
		MonitoringStationChecker create();
	}
}
