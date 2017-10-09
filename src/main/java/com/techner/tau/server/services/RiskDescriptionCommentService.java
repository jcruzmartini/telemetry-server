package com.techner.tau.server.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.techner.tau.server.data.dao.interfaces.RiskDescriptionCommentDao;
import com.techner.tau.server.domain.model.RiskDescriptionComment;

public class RiskDescriptionCommentService {

	private static final Logger logger = LoggerFactory.getLogger(RiskDescriptionCommentService.class);

	private final RiskDescriptionCommentDao dao;

	@Inject
	public RiskDescriptionCommentService(RiskDescriptionCommentDao dao) {
		this.dao = dao;
	}

	public RiskDescriptionComment getRiskComment(Integer idNotification, String risk) {
		logger.debug("Buscando comentario con id de notificacion {} y riesgo {}", idNotification, risk);
		return dao.getRiskComment(idNotification, risk);
	}

}
