package com.techner.tau.server.data.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.techner.tau.server.data.dao.interfaces.UserDao;
import com.techner.tau.server.domain.model.User;

public class UserDaoImpl extends ObjectDAOImpl<User> implements UserDao {

	/** slf4j logger */
	private static final Logger logger = LoggerFactory.getLogger(UserDaoImpl.class);

	@Override
	public List<User> getActiveUsers() {
		logger.debug("Consultando usuarios activos del sistema");
		List<User> users = new ArrayList<User>();
		Session session = getSession();
		try {
			session.beginTransaction();
			Query sqlQ = session.createQuery("from User where active = :active");
			sqlQ.setBoolean("active", Boolean.TRUE);
			users = sqlQ.list();
			session.getTransaction().commit();
		} catch (Exception e) {
			session.getTransaction().rollback();
			logger.error("Error obteniendo todos los usuarios activos", e);
		}
		logger.debug("Se han encontrado {} usuarios activos ", users.size());
		return users;
	}

}
