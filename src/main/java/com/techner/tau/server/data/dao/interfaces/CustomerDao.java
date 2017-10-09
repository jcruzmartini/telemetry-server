package com.techner.tau.server.data.dao.interfaces;

import com.techner.tau.server.domain.model.Customer;

public interface CustomerDao extends ObjectDao<Customer> {
	public Customer findCustomerBySimCardNumber(String simCard);

	public Customer findCustomerByStationSimNumber(String simCard);
}
