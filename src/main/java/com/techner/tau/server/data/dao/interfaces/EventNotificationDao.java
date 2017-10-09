package com.techner.tau.server.data.dao.interfaces;

import com.techner.tau.server.domain.model.EventNotification;

public interface EventNotificationDao extends ObjectDao<EventNotification> {

	EventNotification getLastEventNotificationActive(String codNotification, Integer idStation);
}
