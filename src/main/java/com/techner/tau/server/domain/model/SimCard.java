package com.techner.tau.server.domain.model;


public class SimCard {

	private Integer id;
	private Customer customer;
	private String number;
	private String company;
	private Integer creditDueDay;


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
	public String getNumber() {
		return number;
	}

	/**
	 * @param number
	 */
	public void setNumber(String number) {
		this.number = number;
	}

	/**
	 * @return
	 */
	public String getCompany() {
		return company;
	}

	/**
	 * @param company
	 */
	public void setCompany(String company) {
		this.company = company;
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
	* @return the creditDueDay
	*/
	public Integer getCreditDueDay() {
	    return creditDueDay;
	}

	/**
	* @param creditDueDay the creditDueDay to set
	*/
	public void setCreditDueDay(Integer creditDueDay) {
	    this.creditDueDay = creditDueDay;
	}
}
