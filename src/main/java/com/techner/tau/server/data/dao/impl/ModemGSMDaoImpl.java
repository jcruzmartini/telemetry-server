package com.techner.tau.server.data.dao.impl;

import java.util.Collection;

import org.hibernate.Query;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.techner.tau.server.data.dao.interfaces.ModemGSMDao;
import com.techner.tau.server.domain.model.ModemGSM;

@SuppressWarnings("unchecked")
public class ModemGSMDaoImpl extends ObjectDAOImpl<ModemGSM> implements ModemGSMDao {

	/** slf4j logger */
	private static final Logger logger = LoggerFactory.getLogger(ModemGSMDaoImpl.class);

	public Collection<ModemGSM> getAllModemsEnable() {
		Collection<ModemGSM> modems = null;
		logger.debug("Inside getAllModems()");
		Session session = getSession();
		try {
			session.beginTransaction();
			Query sqlQ = session.createQuery("from ModemGSM where enable = :enable ");
			sqlQ.setBoolean("enable", Boolean.TRUE);
			modems = sqlQ.list();
			session.getTransaction().commit();
		} catch (Exception e) {
			logger.error("Error obteniendo todos los modems registrados ", e);
			session.getTransaction().rollback();
		}
		logger.debug("Exiting getAllModems()");
		return modems;
	}

}
