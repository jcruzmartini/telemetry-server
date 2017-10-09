package com.techner.tau.server.data.dao.interfaces;

import java.util.Collection;

import com.techner.tau.server.domain.model.ModemGSM;

public interface ModemGSMDao extends ObjectDao<ModemGSM> {
	public Collection<ModemGSM> getAllModemsEnable();
}
