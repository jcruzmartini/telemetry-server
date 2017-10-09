package com.techner.tau.server.data.dao.impl;

import org.hibernate.Query;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.techner.tau.server.data.dao.interfaces.ResponseDao;
import com.techner.tau.server.domain.model.Response;

public class ResponseDaoImpl extends ObjectDAOImpl<Response> implements ResponseDao {

	/** slf4j logger */
	private static final Logger logger = LoggerFactory.getLogger(ResponseDaoImpl.class);

	@Override
	public Response getResponseByValue(String value) {
		logger.debug("Buscando Respuesta con valor {} ", value);
		Response response = null;
		Session session = getSession();
		try {
			session.beginTransaction();
			Query sqlQ = session.createQuery("from Response re where re.value = :value");
			sqlQ.setParameter("value", value);
			response = (Response) sqlQ.uniqueResult();
			session.getTransaction().commit();
		} catch (Exception e) {
			session.getTransaction().rollback();
			logger.error("Error obteniendo respuesta con valor {} ", value, e);
		}
		logger.debug("Respuesta con valor {} -> {}", value, response);
		return response;
	}

}
