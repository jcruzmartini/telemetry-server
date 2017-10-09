package com.techner.tau.server.data.dao.impl;

import org.hibernate.Query;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.techner.tau.server.data.dao.interfaces.CustomerDao;
import com.techner.tau.server.domain.model.Customer;

public class CustomerDaoImpl extends ObjectDAOImpl<Customer> implements CustomerDao {

	/** slf4j logger */
	private static final Logger logger = LoggerFactory.getLogger(CustomerDaoImpl.class);

	public Customer findCustomerBySimCardNumber(String number) {
		logger.debug("Inside findCustomerBySimCardNumber(String number) number = " + number);
		Customer customer = null;
		Session session = getSession();
		try {
			session.beginTransaction();
			session.beginTransaction();
			Query sqlQ = session.createQuery("select c.customer from SimCard c where c.number = :simNumber");
			sqlQ.setParameter("simNumber", number);
			customer = (Customer) sqlQ.uniqueResult();
			session.getTransaction().commit();
		} catch (Exception e) {
			session.getTransaction().rollback();
			logger.error("Error obteniendo cliente con numero de sim {} ", e, number);
		}
		logger.debug("Exiting findCustomerBySimCardNumber(String number)");
		return customer;
	}

	public Customer findCustomerByStationSimNumber(String number) {
		logger.debug("Inside findCustomerByStationSimNumber(String number) number = " + number);
		Customer customer = null;
		Session session = getSession();
		try {
			session.beginTransaction();
			session.beginTransaction();
			Query sqlQ = session.createQuery("select c.customer from Station c where c.simCard.number = :simNumber");
			sqlQ.setParameter("simNumber", number);
			customer = (Customer) sqlQ.uniqueResult();
			session.getTransaction().commit();
		} catch (Exception e) {
			session.getTransaction().rollback();
			logger.error("Error obteniendo cliente por numero de sim de la estación . Station sim number {} ", e,
					number);
		}
		logger.debug("Exiting findCustomerByStationSimNumber(String number)");
		return customer;
	}

}
