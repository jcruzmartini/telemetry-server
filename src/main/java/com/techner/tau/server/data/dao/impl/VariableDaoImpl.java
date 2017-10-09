package com.techner.tau.server.data.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.techner.tau.server.data.dao.interfaces.VariableDao;
import com.techner.tau.server.domain.model.Variable;

public class VariableDaoImpl extends ObjectDAOImpl<Variable> implements VariableDao {

	/** slf4j logger */
	private static final Logger logger = LoggerFactory.getLogger(VariableDaoImpl.class);

	public Variable findVariableByCode(String code) {
		logger.debug("Inside findVariableByCode(String code) code = " + code);
		Variable variable = null;
		Session session = getSession();
		try {
			session.beginTransaction();
			Query sqlQ = session.createQuery("from Variable v where v.code=:code");
			sqlQ.setParameter("code", code);
			variable = (Variable) sqlQ.uniqueResult();
			session.getTransaction().commit();
		} catch (Exception e) {
			session.getTransaction().rollback();
			logger.error("Error obteniendo variable con codigo {}", code, e);
		}
		logger.debug("Exiting findVariableByCode(String code) ");
		return variable;
	}

	public List<String> getAllMeteorologicalVariableCodes() {
		logger.debug("Inside getAllMeteorologicalVariableCodes()");
		List<String> variables = new ArrayList<String>();
		Session session = getSession();
		try {
			session.beginTransaction();
			Query sqlQ = session
					.createQuery("select distinct v.code from Variable v where meteorological = :meteorological");
			sqlQ.setParameter("meteorological", Boolean.TRUE);
			variables = sqlQ.list();
			session.getTransaction().commit();
		} catch (Exception e) {
			session.getTransaction().rollback();
			logger.error("Error obteniendo todas las variables meteorologicas", e);
		}
		logger.debug("Exiting getAllMeteorologicalVariableCodes() ");
		return variables;
	}

	public List<String> getAllNonMeteorologicalVariableCodes() {
		logger.debug("Inside getAllNonMeteorologicalVariableCodes()");
		List<String> variables = new ArrayList<String>();
		Session session = getSession();
		try {
			session.beginTransaction();
			Query sqlQ = session
					.createQuery("select distinct v.code from Variable v where meteorological = :meteorological");
			sqlQ.setParameter("meteorological", Boolean.FALSE);
			variables = sqlQ.list();
			session.getTransaction().commit();
		} catch (Exception e) {
			session.getTransaction().rollback();
			logger.error("Error obteniendo todas las variables NO meteorologicas", e);
		}
		logger.debug("Exiting getAllNonMeteorologicalVariableCodes() ");
		return variables;
	}

	@Override
	public List<String> getMeteorologicalVariablesEnableByStation(Integer idStation) {
		logger.debug("Inside getMeteorologicalVariablesEnableByStation()");
		List<String> variables = new ArrayList<String>();
		Session session = getSession();
		try {
			session.beginTransaction();
			Query sqlQ = session
					.createSQLQuery("SELECT V.CODE FROM STATION_VARIABLE SV LEFT JOIN VARIABLE V ON V.ID_VARIABLE = SV.ID_VARIABLE "
							+ "WHERE SV.ID_STATION = :idStation AND SV.ENABLE = :enable");
			sqlQ.setParameter("idStation", idStation);
			sqlQ.setParameter("enable", Boolean.TRUE);
			variables = sqlQ.list();
			session.getTransaction().commit();
		} catch (Exception e) {
			session.getTransaction().rollback();
			logger.error("Error obteniendo todas las variables meteorologicas habilitadas para la estacion {}",
					idStation, e);
		}
		logger.debug("Exiting getMeteorologicalVariablesEnableByStation() ");
		return variables;
	}

}
