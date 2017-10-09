package com.techner.tau.server.domain.model;

import org.smslib.InboundMessage;

import com.techner.tau.server.domain.interfaces.Message;

public class SMSServerOperation extends SMS {

	private Station station;
	private Boolean success;
	
    	/**
     	* Constructor
     	*/
	public SMSServerOperation() {
    	}
	
	public SMSServerOperation(InboundMessage inboundMessage) {
	  super(inboundMessage);
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

	public Boolean isSuccess() {
		return success;
	}

	public void setSuccess(Boolean success) {
		this.success = success;
	}

	public static enum Result {

		OK(1), FAIL(0);

		private Integer result;

		private Result(Integer result) {
			this.result = result;
		}

		public Integer getCode() {
			return result;
		}
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
		return Message.Type.SERVER_OPERATION.getCode();
	}
}
