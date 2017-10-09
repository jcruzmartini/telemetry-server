package com.techner.tau.server.domain.model;

import java.util.Date;

import org.smslib.InboundMessage;

import com.techner.tau.server.domain.interfaces.Message;

public class SMS implements Message {

	private Integer id;
	private Date date;
	private Date sent_at;
	private Date delivered_at;
	private Date received_at;
	private String from;
	private String state;
	private String text;
	private String refNo;
	private String sentByModemId;
	private Integer retries = RETRIES_MAX;
	/**
	 * Usado para mensajes enviados por el SERVIDOR que necesitan una respuesta
	 * por parte de la ETA
	 */
	private Integer tokenNumber = -1;
	/**
	 * Usado para mensajes enviados por la ETA que necesitan una respuesta por
	 * parte del Servidor
	 */
	private String confirmationId;
	private String errorCode;

	public SMS() {
	}

	public SMS(InboundMessage message) {
		this.date = message.getDate();
		this.received_at = new Date();
		this.from = message.getOriginator();
		this.state = Message.Status.PENDING.getCode();
		this.text = message.getText();
	}

	/**
	 * @return
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * @param date
	 */
	public void setDate(Date date) {
		this.date = date;
	}

	/**
	 * @return
	 */
	public String getFrom() {
		return from;
	}

	/**
	 * @param from
	 */
	public void setFrom(String from) {
		this.from = from;
	}

	/**
	 * @return
	 */
	public String getState() {
		return state;
	}

	/**
	 * @param state
	 */
	public void setState(String state) {
		this.state = state;
	}

	/**
	 * @return
	 */
	public String getText() {
		return text;
	}

	/**
	 * @param text
	 */
	public void setText(String text) {
		this.text = text;
	}

	/**
	 * @return
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @param id
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getTokenNumber() {
		return tokenNumber;
	}

	@Override
	public void setTokenNumber(Integer tokenNumber) {
		this.tokenNumber = tokenNumber;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getRefNo() {
		return refNo;
	}

	public void setRefNo(String refNo) {
		this.refNo = refNo;
	}

	public Integer getRetries() {
		return retries;
	}

	public void setRetries(Integer retries) {
		this.retries = retries;
	}

	public Date getSent_at() {
		return sent_at;
	}

	public void setSent_at(Date sent_at) {
		this.sent_at = sent_at;
	}

	public Date getDelivered_at() {
		return delivered_at;
	}

	public void setDelivered_at(Date delivered_at) {
		this.delivered_at = delivered_at;
	}

	public String getSentByModemId() {
		return sentByModemId;
	}

	public void setSentByModemId(String sentByModemId) {
		this.sentByModemId = sentByModemId;
	}

	public Date getReceived_at() {
		return received_at;
	}

	public void setReceived_at(Date received_at) {
		this.received_at = received_at;
	}

	public String getConfirmationId() {
		return confirmationId;
	}

	public void setConfirmationId(String confirmationId) {
		this.confirmationId = confirmationId;
	}

	@Override
	public boolean needsResponse() {
		return false;
	}

	@Override
	public String getType() {
		return null;
	}

}
