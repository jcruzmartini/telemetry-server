package com.techner.tau.server.core.calculator.notification;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.techner.tau.server.domain.model.EventNotification;
import com.techner.tau.server.domain.model.Station;
import com.techner.tau.server.services.EventNotificationService;

public class NotificationCalculatorTaskImpl implements NotificationCalculatorTask {
	/** Id de la estacion a calcular la alerta **/
	private final Station station;
	/** calculator para notificaciones **/
	private final Map<String, NotificationsCalculator> calculators;
	/** Servicio de evento de notificacion */
	private final EventNotificationService notificationEvent;
	/** logger */
	private static final Logger logger = LoggerFactory.getLogger(NotificationCalculatorTaskImpl.class);

	@Inject
	public NotificationCalculatorTaskImpl(@Assisted Station station, Map<String, NotificationsCalculator> calculators,
			EventNotificationService notificationEvent) {
		this.station = station;
		this.calculators = calculators;
		this.notificationEvent = notificationEvent;
	}

	@Override
	public void run() {
		List<EventNotification> notifications = new ArrayList<EventNotification>();

		Set<Entry<String, NotificationsCalculator>> entries = calculators.entrySet();

		for (Entry<String, NotificationsCalculator> entry : entries) {
			List<EventNotification> calculatedNotifications = entry.getValue().calculate(entry.getKey(), station);
			if (!CollectionUtils.isEmpty(calculatedNotifications)) {
				notifications.addAll(calculatedNotifications);
			}
		}

		if (!CollectionUtils.isEmpty(notifications)) {
			logger.info("Almacenando <{}> notificaciones para la estacion con id: {}", notifications.size(),
					station.getId());
			notificationEvent.saveNotifications(notifications);
		}
	}

}
