package com.techner.tau.server.data.dao.interfaces;

import java.util.List;

import com.techner.tau.server.domain.model.User;

public interface UserDao extends ObjectDao<User> {
	public List<User> getActiveUsers();

}
