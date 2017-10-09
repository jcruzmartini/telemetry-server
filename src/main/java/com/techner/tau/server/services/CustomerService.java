package com.techner.tau.server.services;

import com.google.inject.Inject;
import com.techner.tau.server.data.dao.interfaces.CustomerDao;
import com.techner.tau.server.domain.model.Customer;

public class CustomerService {

	private final CustomerDao dao;

	@Inject
	public CustomerService(CustomerDao dao) {
		this.dao = dao;
	}

	public Customer getCustomerByStation(String stationSimNumber) {
		return dao.findCustomerByStationSimNumber(stationSimNumber);
	}

	public Customer getCustomeBySimCard(String simCard) {
		return dao.findCustomerBySimCardNumber(simCard);
	}

	public void saveOrUpdateCustomer(Customer customer) {
		dao.insert(customer);
	}
}
