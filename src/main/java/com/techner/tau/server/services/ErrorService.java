package com.techner.tau.server.services;

import com.google.inject.Inject;
import com.techner.tau.server.data.dao.interfaces.ErrorDao;
import com.techner.tau.server.domain.model.Error;

public class ErrorService {

	private final ErrorDao dao;

	@Inject
	public ErrorService(ErrorDao dao) {
		this.dao = dao;
	}

	public void saveOrUpdate(Error error) {
		dao.insert(error);
	}

	public Error getErrorByCode(String errorCode) {
		return dao.getErrorByCode(errorCode);
	}
}
