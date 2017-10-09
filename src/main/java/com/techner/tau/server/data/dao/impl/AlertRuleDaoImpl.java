package com.techner.tau.server.data.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.techner.tau.server.data.dao.interfaces.AlertRuleDao;
import com.techner.tau.server.domain.model.AlertRule;

public class AlertRuleDaoImpl extends ObjectDAOImpl<AlertRule> implements AlertRuleDao {

	/** slf4j logger */
	private static final Logger logger = LoggerFactory.getLogger(AlertRuleDaoImpl.class);

	public AlertRule findAlertStationByCode(Integer station, String alertCode) {
		logger.debug("Inside findAlertStationByCode(Integer station, String alertCode) station {} , code {}", station,
				alertCode);
		AlertRule alert = null;
		Session session = getSession();
		try {
			session.beginTransaction();

			Criteria crit = session.createCriteria(AlertRule.class);
			crit.createAlias("station", "s");
			crit.createAlias("alert", "a");
			crit.add(Restrictions.eq("station.id", station));
			crit.add(Restrictions.eq("a.code", alertCode));
			crit.setMaxResults(1);

			alert = (AlertRule) crit.uniqueResult();
			session.getTransaction().commit();
		} catch (Exception e) {
			session.getTransaction().rollback();
			logger.error(String.format("Error obteniendo regla de alerta para estación %s con codigo de alerta %a",
					station, alertCode), e);
		}
		logger.debug("Exiting  findAlertStationByCode(Integer station, String alertCode)");
		return alert;
	}

	@Override
	public List<AlertRule> findActiveAlertRulesByChecker(Integer station, String checker) {
		logger.debug("Inside findActiveAlertRulesByChecker(Integer station, String checker) station {} , checker {}",
				station, checker);
		List<AlertRule> alertRules = null;
		Session session = getSession();
		try {
			session.beginTransaction();

			Criteria crit = session.createCriteria(AlertRule.class);
			crit.createAlias("station", "s");
			crit.createAlias("alert", "a");
			crit.add(Restrictions.eq("station.id", station));
			crit.add(Restrictions.eq("a.checkIn", checker));
			crit.add(Restrictions.isNull("endDate"));

			alertRules = crit.list();
			session.getTransaction().commit();
		} catch (Exception e) {
			session.getTransaction().rollback();
			logger.error(String.format("Error obteniendo reglas de alerta activas para estación %s con checker %s",
					station, checker), e);
		}
		logger.debug("Exiting  findActiveAlertRulesByChecker(Integer station, String checker)");
		return alertRules;
	}

}
