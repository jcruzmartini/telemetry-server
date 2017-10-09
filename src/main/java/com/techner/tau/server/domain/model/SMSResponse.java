package com.techner.tau.server.domain.model;

import org.smslib.InboundMessage;

import com.techner.tau.server.domain.interfaces.Message;

public class SMSResponse extends SMS {

	private String to;
	private String friendlyText;

	public SMSResponse(InboundMessage inboundMessage) {
		super(inboundMessage);
	}

	public SMSResponse() {
		super();
	}

	/**
	 * @return
	 */
	public String getTo() {
		return to;
	}

	/**
	 * @param to
	 */
	public void setTo(String to) {
		this.to = to;
	}

	public String getFriendlyText() {
		return friendlyText;
	}

	public void setFriendlyText(String friendlyText) {
		this.friendlyText = friendlyText;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gsm.server.domain.model.SMS#getType()
	 */
	@Override
	public String getType() {
		return Message.Type.RESPONSE.getCode();
	}
}
