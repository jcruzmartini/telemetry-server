package com.techner.tau.server.domain.model;

import org.smslib.InboundMessage;

import com.techner.tau.server.domain.interfaces.Message;

public class SMSStationEvent extends SMS {

	private Customer customer;
	private Station station;
	private String alertCode;
	private String value;

	public SMSStationEvent() {
	}

	public SMSStationEvent(InboundMessage inboundMessage) {
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
	public Station getStation() {
		return station;
	}

	/**
	 * @param station
	 */
	public void setStation(Station station) {
		this.station = station;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getAlertCode() {
		return alertCode;
	}

	public void setAlertCode(String alertCode) {
		this.alertCode = alertCode;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gsm.server.domain.model.SMS#getType()
	 */
	@Override
	public String getType() {
		return Message.Type.ALERT_STATION_EVENT.getCode();
	}

}
