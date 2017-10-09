package com.techner.tau.server.services;

import com.google.inject.Inject;
import com.techner.tau.server.data.dao.interfaces.NotificationDao;
import com.techner.tau.server.domain.model.Notification;

public class NotificationService {

	/** dao object */
	private final NotificationDao dao;

	@Inject
	public NotificationService(NotificationDao dao) {
		this.dao = dao;
	}

	public Notification getNotificationByCode(String code) {
		return dao.findNotificationByCode(code);
	}
}
