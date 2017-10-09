package com.techner.tau.server.services;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.techner.tau.server.data.dao.interfaces.EventNotificationDao;
import com.techner.tau.server.domain.model.EventNotification;

public class EventNotificationService {

	private static final Logger logger = LoggerFactory.getLogger(EventNotificationService.class);

	private final EventNotificationDao dao;

	@Inject
	public EventNotificationService(EventNotificationDao dao) {
		this.dao = dao;
	}

	public void saveOrUpdate(EventNotification eventNotification) {
		dao.insert(eventNotification);
	}

	public void saveNotifications(List<EventNotification> notifications) {
		logger.debug("Almacenando eventos de notificaciones ");
		for (EventNotification notification : notifications) {
			saveOrUpdate(notification);
		}
		logger.info("Fin almacenando eventos de notificaciones");
	}

	public EventNotification getLastEventNotificationActive(String codNotification, Integer idStation) {
		EventNotification eventNotificationActive = dao.getLastEventNotificationActive(codNotification, idStation);
		return eventNotificationActive;
	}
}
