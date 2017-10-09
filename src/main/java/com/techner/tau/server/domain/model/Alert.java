package com.techner.tau.server.domain.model;

public class Alert {

	private Integer id;
	private Variable variable;
	private String code;
	private String alertDescription;
	private String checkIn;

	/**
	 * @return the checkIn
	 */
	public String getCheckIn() {
		return checkIn;
	}

	/**
	 * @param checkIn
	 *            the checkIn to set
	 */
	public void setCheckIn(String checkIn) {
		this.checkIn = checkIn;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getAlertDescription() {
		return alertDescription;
	}

	public void setAlertDescription(String alertDescription) {
		this.alertDescription = alertDescription;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Variable getVariable() {
		return variable;
	}

	public void setVariable(Variable variable) {
		this.variable = variable;
	}
}
