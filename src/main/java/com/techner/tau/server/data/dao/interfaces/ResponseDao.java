package com.techner.tau.server.data.dao.interfaces;

import com.techner.tau.server.domain.model.Response;

public interface ResponseDao extends ObjectDao<Response> {
	Response getResponseByValue(String value);
}
