package com.techner.tau.server.services;

import java.util.List;

import com.google.inject.Inject;
import com.techner.tau.server.data.dao.interfaces.AlertDao;
import com.techner.tau.server.domain.model.Alert;

public class AlertService {

	private final AlertDao dao;

	@Inject
	public AlertService(AlertDao dao) {
		this.dao = dao;
	}

	public Alert getAlertByCode(String code) {
		return dao.findAlertByCode(code);
	}

	public List<String> getAlertTypes() {
		return dao.getAlertTypes();
	}

}
