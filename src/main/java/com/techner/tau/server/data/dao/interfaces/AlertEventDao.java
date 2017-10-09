package com.techner.tau.server.data.dao.interfaces;

import com.techner.tau.server.domain.model.AlertEvent;

public interface AlertEventDao extends ObjectDao<AlertEvent> {
	public AlertEvent getAlertEvent(Integer idAlertRule, Integer stationId);
}
