package com.techner.tau.server.data.dao.impl;

import java.util.Collection;

import org.hibernate.Query;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.techner.tau.server.data.dao.interfaces.ThreadLocalDao;
import com.techner.tau.server.domain.model.LocalThread;

@SuppressWarnings("unchecked")
public class ThreadLocalDaoImpl extends ObjectDAOImpl<LocalThread> implements ThreadLocalDao {

	/** slf4j logger */
	private static final Logger logger = LoggerFactory.getLogger(ThreadLocalDaoImpl.class);

	public Collection<LocalThread> getAllThreadsReceptors() {
		logger.debug("Inside getAllDaemonsReceptors()");
		Collection<LocalThread> threads = null;
		Session session = getSession();
		try {
			session.beginTransaction();
			Query sqlQ = session.createQuery("from LocalThread where type = :type and enable= :enable ");
			sqlQ.setParameter("type", LocalThread.Type.RECEPTOR.getType());
			sqlQ.setBoolean("enable", Boolean.TRUE);
			threads = sqlQ.list();
			session.getTransaction().commit();
		} catch (Exception e) {
			logger.error("Error buscando todos los hilos registrados como receptores", e);
			session.getTransaction().rollback();
		}
		logger.debug("Exiting getAllDaemonsReceptors()");
		return threads;
	}

	public Collection<LocalThread> getAllThreadsSenders() {
		logger.debug("Inside getAllDaemonsSenders()");
		Collection<LocalThread> threads = null;
		Session session = getSession();
		try {
			session.beginTransaction();
			Query sqlQ = session.createQuery("from LocalThread where type = :type and enable= :enable ");
			sqlQ.setParameter("type", LocalThread.Type.SENDER.getType());
			sqlQ.setBoolean("enable", Boolean.TRUE);
			logger.debug("Exiting getAllDaemonsSenders()");
			threads = sqlQ.list();
			session.getTransaction().commit();
		} catch (Exception e) {
			logger.error("Error buscando todos los hilos registrados como enviadores", e);
			session.getTransaction().rollback();
		}
		return threads;
	}

}
