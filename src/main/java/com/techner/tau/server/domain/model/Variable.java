package com.techner.tau.server.domain.model;

public class Variable {

	private Integer id;
	private String description;
	private String smsDescription;
	private String shortDescription;
	private String unit;
	private String code;
	private boolean meteorological = true;

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

	/**
	 * @return
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @return the shortDescription
	 */
	public String getShortDescription() {
		return shortDescription;
	}

	/**
	 * @param shortDescription
	 *            the shortDescription to set
	 */
	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}

	/**
	 * @param description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return
	 */
	public String getUnit() {
		return unit;
	}

	/**
	 * @param unit
	 */
	public void setUnit(String unit) {
		this.unit = unit;
	}

	/**
	 * @return
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @param code
	 */
	public void setCode(String code) {
		this.code = code;
	}

	public boolean isMeteorological() {
		return meteorological;
	}

	public void setMeteorological(boolean meteorological) {
		this.meteorological = meteorological;
	}

	/**
	 * @return the smsDescription
	 */
	public String getSmsDescription() {
		return smsDescription;
	}

	/**
	 * @param smsDescription
	 *            the smsDescription to set
	 */
	public void setSmsDescription(String smsDescription) {
		this.smsDescription = smsDescription;
	}

}
