package com.techner.tau.server.data.dao.impl;

import org.hibernate.Query;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.techner.tau.server.data.dao.interfaces.NotificationDao;
import com.techner.tau.server.domain.model.Notification;

public class NotificationDaoImpl extends ObjectDAOImpl<Notification> implements NotificationDao {

	/** slf4j logger */
	private static final Logger logger = LoggerFactory.getLogger(NotificationDaoImpl.class);

	@Override
	public Notification findNotificationByCode(String code) {
		logger.debug("Inside findNotificationByCode()");
		Notification notification = null;
		Session session = getSession();
		try {
			session.beginTransaction();
			Query sqlQ = session.createQuery("from Notification v where v.code=:code");
			sqlQ.setParameter("code", code);
			notification = (Notification) sqlQ.uniqueResult();
			session.getTransaction().commit();
		} catch (Exception e) {
			logger.error("Error obteniendo notificacion con codigo {} ", code, e);
			session.getTransaction().rollback();
		}
		logger.debug("Exiting findNotificationByCode(String code) ");
		return notification;
	}

}
