package com.techner.tau.server.data.dao.interfaces;

import com.techner.tau.server.domain.model.Notification;

public interface NotificationDao extends ObjectDao<Notification> {
	public Notification findNotificationByCode(String code);
}
