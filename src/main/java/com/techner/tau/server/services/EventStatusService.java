package com.techner.tau.server.services;

import java.util.List;

import com.google.inject.Inject;
import com.techner.tau.server.data.dao.interfaces.EventStatusDao;
import com.techner.tau.server.domain.model.EventStatus;

public class EventStatusService {

	private final EventStatusDao dao;

	@Inject
	public EventStatusService(EventStatusDao dao) {
		this.dao = dao;
	}

	public EventStatus findEventStatusByCode(String code) {
		return dao.findEventStatusByCode(code);
	}

	public List<String> getAllEventStatusCodes() {
		return dao.getAllEventStatusCodes();
	}
}
