package com.techner.tau.server.data.hibernate;

import java.io.File;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HibernateUtil {

	/** slf4j logger */
	private static final Logger logger = LoggerFactory.getLogger(HibernateUtil.class);
	/** hibernate config location */
	private static final String HIBERNATE_CONFIG_LOCATION = "/etc/techner/tau-server/hibernate.cfg.xml";
	/** hibernate config location */
	private static final String HIBERNATE_MAPPING_FOLDER_LOCATION = "/etc/techner/tau-server/mapping";
	/** session factory **/
	private static final SessionFactory sessionFactory;

	static {
		try {
			File file = new File(HIBERNATE_CONFIG_LOCATION);
			File directory = new File(HIBERNATE_MAPPING_FOLDER_LOCATION);
			sessionFactory = new Configuration().configure(file).addDirectory(directory).buildSessionFactory();
		} catch (Throwable ex) {
			// Make sure you log the exception, as it might be swallowed
			logger.error("Initial SessionFactory creation failed.", ex);
			throw new ExceptionInInitializerError(ex);
		}
	}

	public static SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public static Session getSession() {
		return sessionFactory.getCurrentSession();
	}

	public static void closeSession(Session session) {
		if (session != null) {
			if (session.isOpen()) {
				session.close();
			}
		}
	}
}
