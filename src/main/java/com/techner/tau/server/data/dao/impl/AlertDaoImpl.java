package com.techner.tau.server.data.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.techner.tau.server.data.dao.interfaces.AlertDao;
import com.techner.tau.server.domain.model.Alert;

public class AlertDaoImpl extends ObjectDAOImpl<Alert> implements AlertDao {

	/** slf4j logger */
	private static final Logger logger = LoggerFactory.getLogger(AlertDaoImpl.class);

	public Alert findAlertByCode(String code) {
		logger.debug("Inside findAlertByCode(String code) code = " + code);
		Alert alert = null;
		Session session = getSession();
		try {
			session.beginTransaction();
			Query sqlQ = session.createQuery("from Alert v where v.code=:code");
			sqlQ.setParameter("code", code);
			alert = (Alert) sqlQ.uniqueResult();
			session.getTransaction().commit();
		} catch (Exception e) {
			session.getTransaction().rollback();
			logger.error("Error obteniendo alerta con código  {}", e, code);
		}
		logger.debug("Exiting  findAlertByCode(String code)");
		return alert;
	}

	public List<String> getAlertTypes() {
		logger.debug("Inside getAlertTypes()");
		List<String> alertTypes = new ArrayList<String>();
		Session session = getSession();
		try {
			session.beginTransaction();
			Query sqlQ = session.createQuery("select distinct a.code from Alert a ");
			alertTypes = sqlQ.list();
			session.getTransaction().commit();
		} catch (Exception e) {
			session.getTransaction().rollback();
			logger.error("Error obteniendo tipos de alerta ", e);
		}
		logger.debug("Exiting  getAlertTypes()");
		return alertTypes;
	}

}
