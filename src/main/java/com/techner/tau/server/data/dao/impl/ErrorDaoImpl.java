package com.techner.tau.server.data.dao.impl;

import org.hibernate.Query;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.techner.tau.server.data.dao.interfaces.ErrorDao;
import com.techner.tau.server.domain.model.Error;

public class ErrorDaoImpl extends ObjectDAOImpl<Error> implements ErrorDao {

	/** slf4j logger */
	private static final Logger logger = LoggerFactory.getLogger(ErrorDaoImpl.class);

	@Override
	public Error getErrorByCode(String errorCode) {
		logger.debug("Buscando Error con codigo {} ", errorCode);
		Error error = null;
		Session session = getSession();
		try {
			session.beginTransaction();
			Query sqlQ = session.createQuery("from Error er where er.codError = :errorCode");
			sqlQ.setParameter("errorCode", errorCode);
			error = (Error) sqlQ.uniqueResult();
			session.getTransaction().commit();
		} catch (Exception e) {
			session.getTransaction().rollback();
			logger.error("Error obteniendo error con código {} ", e, errorCode);
		}
		logger.debug("Error con codigo de error {} -> {}", errorCode, error);
		return error;
	}

}
