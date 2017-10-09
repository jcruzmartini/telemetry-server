package com.techner.tau.server.data.dao.impl;

import java.util.Collection;

import org.hibernate.HibernateException;
import org.hibernate.LockOptions;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.techner.tau.server.data.dao.interfaces.ObjectDao;
import com.techner.tau.server.data.hibernate.HibernateUtil;

@SuppressWarnings("unchecked")
public class ObjectDAOImpl<T> implements ObjectDao<T> {

	/** slf4j logger */
	private static final Logger logger = LoggerFactory.getLogger(ObjectDAOImpl.class);

	public void delete(T entity) {
		logger.debug("Deleting object: " + entity.getClass().toString());
		Session session = getSession();
		Transaction tx = session.getTransaction();
		try {
			tx.begin();
			session.delete(entity);
			tx.commit();
			logger.debug("Object deleted correcly: " + entity.getClass().toString());
		} catch (Exception e) {
			logger.error("Error Deleting object: ", e);
			if (tx != null) {
				try {
					tx.rollback();
				} catch (HibernateException e1) {
					logger.error("Error Deleting object: ", e1);
				}
			}
		}
	}

	public T findByCode(T entity, Long codigo, boolean lock) {
		if (lock) {
			return (T) getSession().load(entity.getClass(), codigo, LockOptions.UPGRADE);
		} else {
			return (T) getSession().load(entity.getClass(), codigo);
		}
	}

	public void insert(T entity) {
		logger.debug("Saving or Updating object: " + entity.getClass().toString());
		Session session = getSession();
		Transaction tx = session.getTransaction();
		try {
			tx.begin();
			session.saveOrUpdate(entity);
			tx.commit();
			logger.debug("Object saved or updated correctly: " + entity.getClass().toString());
		} catch (Exception e) {
			logger.error("Error Saving or Updating object: ", e);
			if (tx != null) {
				try {
					tx.rollback();
				} catch (HibernateException e1) {
					logger.error("Error Saving or Updating object: ", e1);
				}
			}
		}
	}

	public Collection<T> findByParameters(String nombreQuery, String[] parameterNames, Object[] parameterValues) {
		Session session = getSession();
		Query query = (Query) session.getNamedQuery(nombreQuery);
		setParameters(query, parameterNames, parameterValues);
		return query.list();
	}

	private void setParameters(Query query, String[] parameterNames, Object[] parameterValues)
			throws HibernateException {
		for (int n = 0; n < parameterNames.length; n++) {
			if (parameterValues[n] instanceof Collection) {
				query.setParameterList(parameterNames[n], (Collection<?>) parameterValues[n]);
			} else {
				query.setParameter(parameterNames[n], parameterValues[n]);
			}
		}
	}

	public Collection<T> findByQuery(String queryStr) {
		Session session = getSession();
		Query query = session.createQuery(queryStr);
		return query.list();
	}

	public Session getSession() {
		return HibernateUtil.getSession();
	}
}
