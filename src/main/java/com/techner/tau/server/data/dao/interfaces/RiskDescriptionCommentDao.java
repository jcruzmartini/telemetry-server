package com.techner.tau.server.data.dao.interfaces;

import com.techner.tau.server.domain.model.RiskDescriptionComment;

public interface RiskDescriptionCommentDao extends ObjectDao<RiskDescriptionComment> {

	public RiskDescriptionComment getRiskComment(Integer idNotification, String riks);
}
