package com.techner.tau.server.core.periodicalnotifications;

public interface PeriodicalNotificationsChecker extends Runnable {

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
		PeriodicalNotificationsChecker create();
	}
}
