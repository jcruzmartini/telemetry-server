package com.techner.tau.server.domain.model;

import org.smslib.InboundMessage;

import com.techner.tau.server.domain.interfaces.Message;

public class SMSCustomerRequest extends SMS {

	private Customer customer;
	private SMSResponse response;
	private Station station;

	public SMSCustomerRequest() {
	}

	public SMSCustomerRequest(InboundMessage inboundMessage) {
		super(inboundMessage);
	}

	/**
	 * @return
	 */
	public Customer getCustomer() {
		return customer;
	}

	/**
	 * @param customer
	 */
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	/**
	 * @return
	 */
	public SMSResponse getResponse() {
		return response;
	}

	/**
	 * @param response
	 */
	public void setResponse(SMSResponse response) {
		this.response = response;
	}

	/**
	 * @return
	 */
	public Station getStation() {
		return station;
	}

	/**
	 * @param station
	 */
	public void setStation(Station station) {
		this.station = station;
	}

	@Override
	public boolean needsResponse() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gsm.server.domain.model.SMS#getType()
	 */
	@Override
	public String getType() {
		return Message.Type.CUSTOMER_REQUEST.getCode();
	}

}
