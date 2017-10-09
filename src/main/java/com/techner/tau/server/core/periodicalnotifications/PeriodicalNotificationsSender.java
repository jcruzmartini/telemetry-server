package com.techner.tau.server.core.periodicalnotifications;

import com.google.inject.assistedinject.Assisted;
import com.techner.tau.server.domain.model.PeriodicalNotification;

public interface PeriodicalNotificationsSender extends Runnable {

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
		PeriodicalNotificationsSender create(@Assisted PeriodicalNotification notification);
	}
}
