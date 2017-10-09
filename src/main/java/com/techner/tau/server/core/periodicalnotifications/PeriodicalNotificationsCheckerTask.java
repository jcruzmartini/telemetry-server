package com.techner.tau.server.core.periodicalnotifications;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ThreadPoolExecutor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.techner.tau.server.core.annotation.PeriodicalNotificationsPoolExecutor;
import com.techner.tau.server.domain.model.PeriodicalNotification;
import com.techner.tau.server.services.StationService;

public class PeriodicalNotificationsCheckerTask implements PeriodicalNotificationsChecker {

	/** slf4j logger */
	private static final Logger logger = LoggerFactory.getLogger(PeriodicalNotificationsCheckerTask.class);
	/** station service */
	private final StationService service;
	/** factory for sender notificaciones periodicas */
	private final PeriodicalNotificationsSender.Factory factory;
	/** executor */
	private final ThreadPoolExecutor exec;

	@Inject
	public PeriodicalNotificationsCheckerTask(StationService service, PeriodicalNotificationsSender.Factory factory,
			@PeriodicalNotificationsPoolExecutor ThreadPoolExecutor exec) {
		this.service = service;
		this.factory = factory;
		this.exec = exec;
	}

	@Override
	public void run() {
		Calendar now = getBeginCalendar();
		now.set(Calendar.MINUTE, 0);
		now.set(Calendar.SECOND, 0);
		Date date = now.getTime();
		String day = now.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.US).toLowerCase();

		List<PeriodicalNotification> stationsInNotification = service.getStationsInNotificationTime(day, date);
		logger.info("Encontradas [{}] estaciones subscriptas a notificaciones periodicas el dia/hora [{}]",
				stationsInNotification.size(), date);
		for (PeriodicalNotification notification : stationsInNotification) {
			PeriodicalNotificationsSender task = factory.create(notification);
			exec.execute(task);
		}
	}

	/**
	 * Create begin calendar
	 * 
	 * @return calendar
	 */
	private Calendar getBeginCalendar() {
		Calendar beginCalendar = Calendar.getInstance();
		beginCalendar.setTime(new Date());
		return beginCalendar;
	}
}
