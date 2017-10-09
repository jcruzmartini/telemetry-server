package com.techner.tau.server.data.dao.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.LockOptions;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import scala.Array;

import com.techner.tau.server.data.dao.interfaces.SmsDao;
import com.techner.tau.server.domain.model.SMS;
import com.techner.tau.server.domain.model.SMSCustomerRequest;
import com.techner.tau.server.domain.model.SMSServerOperation;

public class SmsDaoImpl extends ObjectDAOImpl<SMS> implements SmsDao {

	/** slf4j logger */
	private static final Logger logger = LoggerFactory.getLogger(SmsDaoImpl.class);

	public SMSCustomerRequest findLastSmsInWaitingForResponse(Integer id, Integer token) {
		SMSCustomerRequest sms = null;
		logger.debug("Inside findLastSmsInWaitingForResponse(id) with id = " + id);
		Session session = getSession();
		try {
			session.beginTransaction();
			// Chequeamos si viene como pedido de un cliente directamente por
			// SMS
			Query sqlQ = session
					.createQuery("from SMSCustomerRequest sc where sc.station.id = :id and sc.state =:state and sc.tokenNumber = :token");
			sqlQ.setInteger("id", id);
			sqlQ.setInteger("token", token);
			sqlQ.setString("state", SMS.Status.WAITING_STATION_RESPONSE.getCode());
			sms = (SMSCustomerRequest) sqlQ.uniqueResult();
			session.getTransaction().commit();
		} catch (Exception e) {
			logger.error(String.format(
					"Error obteniendo sms en WAITING_STATION_RESPONSE para la estacion %s con token %s ", id, token), e);
			session.getTransaction().rollback();
		}
		logger.debug("Exiting findLastSmsInWaitingForResponse(id)");
		return sms;
	}

	public SMS findFirstSMSWaiting() {
		SMS sms = null;
		logger.debug("Inside findFirstSMSWaiting");
		Session session = getSession();
		try {
			session.beginTransaction();
			Criteria crit = session.createCriteria(SMS.class);
			crit.add(Restrictions.eq("state", SMS.Status.PENDING.getCode()));
			crit.addOrder(Order.asc("date"));
			crit.setMaxResults(1);
			sms = (SMS) crit.uniqueResult();
			if (sms != null) {
				logger.debug("Locking for UPDATE SMS with id#: " + sms.getId());
				session.buildLockRequest(LockOptions.UPGRADE).lock(sms);
				sms.setState(SMS.Status.SENDING.getCode());
				session.saveOrUpdate(sms);
			}
			session.getTransaction().commit();
		} catch (Exception e) {
			logger.error("Error obteniendo mensaje en estado PENDING", e);
			session.getTransaction().rollback();
		}
		logger.debug("Exiting findLastSmsInWaitingForResponse(id)");
		return sms;
	}

	public Long countSMSRequestSentByCustomer(Integer customerId) {
		Long count = 0l;
		Calendar calTo = Calendar.getInstance();
		calTo.setTime(new Date());
		calTo.add(Calendar.HOUR, -24);
		logger.debug("Inside countSMSRequestSentByCustomer(Integer customerId) with customerId= " + customerId);
		Session session = getSession();
		try {
			session.beginTransaction();
			Criteria crit = session.createCriteria(SMSCustomerRequest.class);
			crit.add(Restrictions.ne("state", SMS.Status.FAILED.getCode()));
			crit.add(Restrictions.eq("customer.id", customerId));
			crit.add(Restrictions.between("date", calTo.getTime(), new Date()));
			crit.setProjection(Projections.rowCount());
			count = (Long) crit.uniqueResult();
			session.getTransaction().commit();
		} catch (Exception e) {
			logger.error("Error contando la cant. de sms enviados por cliente con id{} ", customerId, e);
			session.getTransaction().rollback();
		}
		logger.debug("Exiting countSMSRequestSentByCustomer(Integer customerId). Total count: " + count);
		return count;
	}

	@Override
	public SMSServerOperation getOperationInWaitingResponse(String originator, Integer tokenNumber) {
		SMSServerOperation sms = null;
		logger.debug("Inside getOperationInWaitingResponse");
		Session session = getSession();
		try {
			session.beginTransaction();
			Query sqlQ = session
					.createQuery("from SMSServerOperation so where so.station.simCard.number = :number and so.tokenNumber =:token and so.state =:state");
			sqlQ.setInteger("token", tokenNumber);
			sqlQ.setString("number", originator);
			sqlQ.setString("state", SMS.Status.WAITING_STATION_RESPONSE.getCode());
			sms = (SMSServerOperation) sqlQ.uniqueResult();
			session.getTransaction().commit();
		} catch (Exception e) {
			logger.error(String.format(
					"Error obteniendo sms de operacion en WAITING_STATION_RESPONSE para la sim %s con token %s ",
					originator, tokenNumber), e);
			session.getTransaction().rollback();
		}
		logger.debug("Exiting findLastSmsInWaitingForResponse(id)");
		return sms;
	}

	@Override
	public SMSCustomerRequest getSMStInWaitingResponse(String originator, Integer tokenNumber) {
		SMSCustomerRequest sms = null;
		logger.debug("Inside getSMStInWaitingResponse");
		Session session = getSession();
		try {
			session.beginTransaction();
			Query sqlQ = session.createQuery("from SMS so where so.tokenNumber =:token and so.state =:state");
			sqlQ.setInteger("token", tokenNumber);
			sqlQ.setString("state", SMS.Status.WAITING_STATION_RESPONSE.getCode());
			sms = (SMSCustomerRequest) sqlQ.uniqueResult();
			session.getTransaction().commit();
		} catch (Exception e) {
			logger.error(String.format("Error obteniendo sms en WAITING_STATION_RESPONSE para la sim %s con token %s ",
					originator, tokenNumber), e);
			session.getTransaction().rollback();
		}
		logger.debug("Exiting getSMStInWaitingResponse(id)");
		return sms;
	}
	
        @Override
        public List<SMS> getAllSMSWaitingResponse() {
            List<SMS> sms = new ArrayList<SMS>();
            logger.debug("Inside getAllSMSWaitingResponse");
            Session session = getSession();
            try {
                session.beginTransaction();
                Criteria crit = session.createCriteria(SMS.class);
                crit.add(Restrictions.eq("state", SMS.Status.WAITING_STATION_RESPONSE.getCode()));
                sms = crit.list();
                session.getTransaction().commit();
            } catch (Exception e) {
                logger.error(String.format("Error obteniendo todos los sms en WAITING_STATION_RESPONSE", e));
                session.getTransaction().rollback();
            }
            logger.debug("Exiting getAllSMSWaitingResponse(id)");
            return sms;
        }

	@Override
	public SMS getSMSByRefNo(int refNo, String modemId) {
		SMS sms = null;
		logger.debug("Inside getSMSByRefNo");
		Session session = getSession();
		try {
			session.beginTransaction();
			Query sqlQ = session.createQuery("from SMS so where so.refNo =:refNumber and so.sentByModemId =:modemId");
			sqlQ.setInteger("refNumber", refNo);
			sqlQ.setString("modemId", modemId);
			sms = (SMS) sqlQ.uniqueResult();
			session.getTransaction().commit();
		} catch (Exception e) {
			logger.error(String.format("Error obteniendo sms por ref number. Id del modem %s, ref number %s ", modemId,
					refNo), e);
			session.getTransaction().rollback();
		}
		logger.debug("Exiting getSMSByRefNo(int refNo)");
		return sms;
	}

	@Override
	public int getRetriesQty(int smsId) {
		int qty = 0;
		logger.debug("Inside getRetriesQty");
		Session session = getSession();
		try {
			session.beginTransaction();
			Query sqlQ = session.createQuery("select retries from SMS so where so.id =:smsId and so.sent_at ");
			sqlQ.setInteger("smsId", smsId);
			qty = (Integer) sqlQ.uniqueResult();
			logger.debug("Exiting getRetriesQty()");
			session.getTransaction().commit();
		} catch (Exception e) {
			logger.error("Error obteniendo cantidad de retries por sms. Con smsId {} ", smsId, e);
			session.getTransaction().rollback();
		}
		return qty;
	}
}
