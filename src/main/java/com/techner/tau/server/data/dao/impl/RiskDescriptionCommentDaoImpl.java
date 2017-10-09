package com.techner.tau.server.data.dao.impl;

import org.hibernate.Query;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.techner.tau.server.data.dao.interfaces.RiskDescriptionCommentDao;
import com.techner.tau.server.domain.model.RiskDescriptionComment;

public class RiskDescriptionCommentDaoImpl extends ObjectDAOImpl<RiskDescriptionComment> implements
		RiskDescriptionCommentDao {

	/** slf4j logger */
	private static final Logger logger = LoggerFactory.getLogger(RiskDescriptionCommentDaoImpl.class);

	@Override
	public RiskDescriptionComment getRiskComment(Integer idNotification, String risk) {
		RiskDescriptionComment comment = null;
		logger.debug("Buscando comentario para notificacion con id {} y con riesgo {} ", idNotification, risk);
		Session session = getSession();
		try {
			session.beginTransaction();
			Query sqlQ = session
					.createQuery("from RiskDescriptionComment co where co.notification.id = :idNotification and co.risk = :risk");
			sqlQ.setParameter("idNotification", idNotification);
			sqlQ.setParameter("risk", risk);
			comment = (RiskDescriptionComment) sqlQ.uniqueResult();
			session.getTransaction().commit();
		} catch (Exception e) {
			session.getTransaction().rollback();
			logger.error(String.format("Buscando comentario para notificacion con id %s y con riesgo %s",
					idNotification, risk), e);
		}
		return comment;
	}

}
