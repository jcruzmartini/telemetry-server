package com.techner.tau.server.data.dao.interfaces;

import java.util.List;

import com.techner.tau.server.domain.model.EventStatus;

public interface EventStatusDao extends ObjectDao<EventStatus> {
	public EventStatus findEventStatusByCode(String code);

	public List<String> getAllEventStatusCodes();
}
