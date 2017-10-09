package com.techner.tau.server.domain.model;

public class EventStatus {

	/** id */
	private Integer id;
	/** code */
	private String code;
	/** description */
	private String description;
	/** Critical */
	private Boolean critical;

	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @param code
	 *            the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the critical
	 */
	public Boolean isCritical() {
		return critical;
	}

	/**
	 * @param critical
	 *            the critical to set
	 */
	public void setCritical(Boolean critical) {
		this.critical = critical;
	}

}
