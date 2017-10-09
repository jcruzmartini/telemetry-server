package com.techner.tau.server.data.dao.interfaces;

import java.util.Collection;

import com.techner.tau.server.domain.model.LocalThread;

public interface ThreadLocalDao extends ObjectDao<LocalThread> {
	public Collection<LocalThread> getAllThreadsReceptors();

	public Collection<LocalThread> getAllThreadsSenders();
}
