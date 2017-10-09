package com.techner.tau.server.data.dao.interfaces;

import com.techner.tau.server.domain.model.Error;

public interface ErrorDao extends ObjectDao<Error> {
	Error getErrorByCode(String errorCode);
}
