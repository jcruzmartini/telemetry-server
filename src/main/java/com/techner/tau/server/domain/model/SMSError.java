package com.techner.tau.server.domain.model;

import org.smslib.InboundMessage;

import com.techner.tau.server.domain.interfaces.Message;

public class SMSError extends SMS {

	private Error error;
	private Station station;

	public SMSError(InboundMessage inboundMessage) {
		super(inboundMessage);
	}

	/**
	 * Default Constructor
	 */
	public SMSError() {
	}

	/**
	 * @return the station
	 */
	public Station getStation() {
		return station;
	}

	/**
	 * @param station
	 *            the station to set
	 */
	public void setStation(Station station) {
		this.station = station;
	}

	/**
	 * @return the error
	 */
	public Error getError() {
		return error;
	}

	/**
	 * @param error
	 *            the error to set
	 */
	public void setError(Error error) {
		this.error = error;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gsm.server.domain.model.SMS#getType()
	 */
	@Override
	public String getType() {
		return Message.Type.ERROR.getCode();
	}

}
