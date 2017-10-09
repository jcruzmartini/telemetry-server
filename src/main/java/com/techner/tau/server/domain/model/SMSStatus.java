package com.techner.tau.server.domain.model;

import java.util.Date;

import org.smslib.InboundMessage;

import com.techner.tau.server.domain.interfaces.Message;

public class SMSStatus extends SMS {

	private Station station;
	private EventStatus event;
	private String value;
	private Date date;

	public SMSStatus(InboundMessage inboundMessage) {
		super(inboundMessage);
	}

	public SMSStatus() {
		super();
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
	 * @return the event
	 */
	public EventStatus getEvent() {
		return event;
	}

	/**
	 * @param event
	 *            the event to set
	 */
	public void setEvent(EventStatus event) {
		this.event = event;
	}

	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @param value
	 *            the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * @return the date
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * @param date
	 *            the date to set
	 */
	public void setDate(Date date) {
		this.date = date;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gsm.server.domain.model.SMS#getType()
	 */
	@Override
	public String getType() {
		return Message.Type.STATUS.getCode();
	}

}
