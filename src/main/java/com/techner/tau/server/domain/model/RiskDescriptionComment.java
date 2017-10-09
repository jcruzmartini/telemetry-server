package com.techner.tau.server.domain.model;

public class RiskDescriptionComment {

	private Integer id;
	private Notification notification;
	private String risk;
	private String description;

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
	 * @return the notification
	 */
	public Notification getNotification() {
		return notification;
	}

	/**
	 * @param notification
	 *            the notification to set
	 */
	public void setNotification(Notification notification) {
		this.notification = notification;
	}

	/**
	 * @return the risk
	 */
	public String getRisk() {
		return risk;
	}

	/**
	 * @param risk
	 *            the risk to set
	 */
	public void setRisk(String risk) {
		this.risk = risk;
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

}
