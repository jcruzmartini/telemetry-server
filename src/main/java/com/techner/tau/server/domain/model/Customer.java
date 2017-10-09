package com.techner.tau.server.domain.model;

import java.util.List;

public class Customer {

	/**
     */
	private Integer id;
	/**
     */
	private String name;
	/**
     */
	private String lastName;
	/**
     */
	private List<Email> emails;
	/**
     */
	private String address;
	/**
     */
	private String city;
	/**
     */
	private String status;
	/**
     */
	private String province;
	/**
     */
	private List<SimCard> simCards;
	/**
     */
	private List<Station> stations;

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

	public List<Station> getStations() {
		return stations;
	}

	public void setStations(List<Station> stations) {
		this.stations = stations;
	}

	public List<SimCard> getSimCards() {
		return simCards;
	}

	public void setSimCards(List<SimCard> simCards) {
		this.simCards = simCards;
	}

	/**
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * @param lastName
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * @return
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * @param address
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * @return
	 */
	public String getCity() {
		return city;
	}

	/**
	 * @param city
	 */
	public void setCity(String city) {
		this.city = city;
	}

	/**
	 * @return
	 */
	public String getProvince() {
		return province;
	}

	/**
	 * @param province
	 */
	public void setProvince(String province) {
		this.province = province;
	}

	/**
	 * @return the emails
	 */
	public List<Email> getEmails() {
		return emails;
	}

	/**
	 * @param emails
	 *            the emails to set
	 */
	public void setEmails(List<Email> emails) {
		this.emails = emails;
	}

	/**
	 * @return
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	public static enum Status {

		ENABLED("E"), SUSPENDED("S");

		private String status;

		private Status(String status) {
			this.status = status;
		}

		public String getCode() {
			return status;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Customer [id=" + id + ", name=" + name + ", lastName=" + lastName + ", emails=" + emails + "]";
	}
}
