package com.techner.tau.server.services;

import com.google.inject.Inject;
import com.techner.tau.server.data.dao.interfaces.ResponseDao;
import com.techner.tau.server.domain.model.Response;

public class ResponseService {

	private final ResponseDao dao;

	@Inject
	public ResponseService(ResponseDao dao) {
		this.dao = dao;
	}

	public void saveOrUpdate(Response response) {
		dao.insert(response);
	}

	public Response getResponseByValue(String response) {
		return dao.getResponseByValue(response);
	}
}
