package com.techner.tau.server.domain.model;

public class Station {

	/** id */
	private Integer id;
	/** SimCard info */
	private SimCard simCard;
	/** Status */
	private boolean enable;
	/** Cliente */
	private Customer customer;
	/** Informacion adicional **/
	private AditionalInformation information;

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
	 * @return the enable
	 */
	public boolean isEnable() {
		return enable;
	}

	/**
	 * @param enable
	 *            the enable to set
	 */
	public void setEnable(boolean enable) {
		this.enable = enable;
	}

	/**
	 * @return
	 */
	public Customer getCustomer() {
		return customer;
	}

	/**
	 * @param customer
	 */
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	/**
	 * @return
	 */
	public SimCard getSimCard() {
		return simCard;
	}

	/**
	 * @param simCard
	 */
	public void setSimCard(SimCard simCard) {
		this.simCard = simCard;
	}

	/**
	 * @return the information
	 */
	public AditionalInformation getInformation() {
		return information;
	}

	/**
	 * @param information
	 *            the information to set
	 */
	public void setInformation(AditionalInformation information) {
		this.information = information;
	}
	
	
}
