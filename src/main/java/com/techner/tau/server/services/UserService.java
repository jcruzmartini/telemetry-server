package com.techner.tau.server.services;

import java.util.List;

import com.google.inject.Inject;
import com.techner.tau.server.data.dao.interfaces.UserDao;
import com.techner.tau.server.domain.model.User;

public class UserService {

	private final UserDao dao;

	@Inject
	public UserService(UserDao dao) {
		this.dao = dao;
	}

	public List<User> getActiveUsers() {
		return dao.getActiveUsers();
	}
}
