package com.techner.tau.server.domain.model;

import org.smslib.InboundMessage;

import com.techner.tau.server.domain.interfaces.Message;

public class SMSDailySummary extends SMS {

	public SMSDailySummary(InboundMessage inboundMessage) {
		super(inboundMessage);
	}

	public SMSDailySummary() {
	}

	private String test;

	public String getTest() {
		return test;
	}

	public void setTest(String test) {
		this.test = test;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gsm.server.domain.model.SMS#getType()
	 */
	@Override
	public String getType() {
		return Message.Type.DAILY_SUMMARY.getCode();
	}

}
