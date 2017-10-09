package com.techner.tau.server.data.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.techner.tau.server.data.dao.interfaces.EventStatusDao;
import com.techner.tau.server.domain.model.EventStatus;

public class EventStatusDaoImpl extends ObjectDAOImpl<EventStatus> implements EventStatusDao {

	/** slf4j logger */
	private static final Logger logger = LoggerFactory.getLogger(EventStatusDaoImpl.class);

	public EventStatus findEventStatusByCode(String code) {
		logger.debug("Inside findEventStatusByCode(String code) code = " + code);
		EventStatus event = null;
		Session session = getSession();
		try {
			session.beginTransaction();
			Query sqlQ = session.createQuery("from EventStatus v where v.code=:code");
			sqlQ.setParameter("code", code);
			event = (EventStatus) sqlQ.uniqueResult();
			session.getTransaction().commit();
		} catch (Exception e) {
			session.getTransaction().rollback();
			logger.error("Error obteniendo evento de estado con codigo {}", e, code);
		}
		logger.debug("Exiting  findEventStatusByCode(String code)");
		return event;
	}

	@Override
	public List<String> getAllEventStatusCodes() {
		logger.debug("Inside getAllEventStatusCodes()");
		List<String> events = new ArrayList<String>();
		Session session = getSession();
		try {
			session.beginTransaction();
			Query sqlQ = session.createQuery("select distinct e.code from EventStatus e");
			events = sqlQ.list();
			session.getTransaction().commit();
		} catch (Exception e) {
			session.getTransaction().rollback();
			logger.error("Error obteniendo todos los codigos de eventos", e);
		}
		logger.debug("Exiting getAllEventStatusCodes() ");
		return events;
	}
}
