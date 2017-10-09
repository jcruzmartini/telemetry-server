package com.techner.tau.server.data.dao.impl;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.techner.tau.server.data.dao.interfaces.EventNotificationDao;
import com.techner.tau.server.domain.model.EventNotification;

public class EventNotificationDaoImpl extends ObjectDAOImpl<EventNotification> implements EventNotificationDao {

	/** slf4j logger */
	private static final Logger logger = LoggerFactory.getLogger(EventNotificationDaoImpl.class);

	public EventNotification getLastEventNotificationActive(String codNotification, Integer idStation) {
		EventNotification eventNotificationActive = null;
		Session session = getSession();
		try {
			session.beginTransaction();
			Criteria crit = session.createCriteria(EventNotification.class);
			crit.createAlias("comment", "c");
			crit.createAlias("c.notification", "n");
			crit.add(Restrictions.eq("station.id", idStation));
			crit.add(Restrictions.eq("n.code", codNotification));
			crit.add(Restrictions.isNull("endDate"));
			crit.setMaxResults(1);
			eventNotificationActive = (EventNotification) crit.uniqueResult();
			session.getTransaction().commit();
			logger.debug("Buscando una notificacion de evento aun activa con codigo {} y idStation {}",
					codNotification, idStation);
		} catch (Exception e) {
			session.getTransaction().rollback();
			logger.error("Error obteniendo evento de notificacion activo", e);
		}
		return eventNotificationActive;
	}
}
