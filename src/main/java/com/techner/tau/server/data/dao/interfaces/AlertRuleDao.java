package com.techner.tau.server.data.dao.interfaces;

import java.util.List;

import com.techner.tau.server.domain.model.AlertRule;

public interface AlertRuleDao extends ObjectDao<AlertRule> {
	public AlertRule findAlertStationByCode(Integer station, String alertCode);

	public List<AlertRule> findActiveAlertRulesByChecker(Integer station, String checker);
}
