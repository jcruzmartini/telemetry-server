package com.techner.tau.server.data.dao.interfaces;

import java.util.List;

import com.techner.tau.server.domain.model.Alert;

public interface AlertDao extends ObjectDao<Alert> {
	/**station checker*/
	String CHECKER_STATION = "STATION";
	/**server cheker*/
	String CHECKER_SERVER = "SERVER";

	public Alert findAlertByCode(String code);

	public List<String> getAlertTypes();
}
