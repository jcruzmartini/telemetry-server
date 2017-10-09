package com.techner.tau.server.data.dao.impl;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.techner.tau.server.data.dao.interfaces.AlertEventDao;
import com.techner.tau.server.domain.model.AlertEvent;

public class AlertEventDaoImpl extends ObjectDAOImpl<AlertEvent> implements AlertEventDao {
	/** slf4j logger */
	private static final Logger logger = LoggerFactory.getLogger(AlertEventDaoImpl.class);

	@Override
	public AlertEvent getAlertEvent(Integer idAlertRule, Integer stationId) {
		AlertEvent event = null;
		Session session = getSession();
		try {
			session.beginTransaction();
	                Criteria crit = session.createCriteria(AlertEvent.class);
	                crit.createAlias("rule", "r");
                        crit.add(Restrictions.eq("r.station.id", stationId));
                        crit.add(Restrictions.eq("r.id", idAlertRule));
                        crit.addOrder(Order.desc("lastUpdate"));
                        crit.setMaxResults(1);
                        
			event = (AlertEvent) crit.uniqueResult();

			session.getTransaction().commit();
		} catch (Exception e) {
			session.getTransaction().rollback();
			logger.error("Error obteniendo regla de alerta con id " + idAlertRule, e);
		}
		return event;
	}
}
