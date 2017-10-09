package com.techner.tau.server.domain.model;

import java.util.Date;

public class SunriseSunsetInformation {

	/** Id */
	private Integer id;
	/** Aditional info */
	private AditionalInformation information;
	/** sunset time */
	private Date sunsetTime;
	/** sunrise time */
	private Date sunriseTime;
	/** date */
	private Date date;

	/**
	 * Constructor
	 */
	public SunriseSunsetInformation() {
	}

	/**
	 * Constructor
	 * 
	 * @param id
	 *            id
	 * @param information
	 *            information
	 * @param sunsetTime
	 *            hora de salida del sol
	 * @param sunriseTime
	 *            hora de puesta del sol
	 * @param date
	 *            fecha
	 */
	public SunriseSunsetInformation(AditionalInformation information, Date sunsetTime, Date sunriseTime, Date date) {
		this.information = information;
		this.sunsetTime = sunsetTime;
		this.sunriseTime = sunriseTime;
		this.date = date;
	}

	/**
	 * @return the sunsetTime
	 */
	public Date getSunsetTime() {
		return sunsetTime;
	}

	/**
	 * @param sunsetTime
	 *            the sunsetTime to set
	 */
	public void setSunsetTime(Date sunsetTime) {
		this.sunsetTime = sunsetTime;
	}

	/**
	 * @return the sunriseTime
	 */
	public Date getSunriseTime() {
		return sunriseTime;
	}

	/**
	 * @param sunriseTime
	 *            the sunriseTime to set
	 */
	public void setSunriseTime(Date sunriseTime) {
		this.sunriseTime = sunriseTime;
	}

	/**
	 * @return the date
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * @param date
	 *            the date to set
	 */
	public void setDate(Date date) {
		this.date = date;
	}

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

	public AditionalInformation getInformation() {
		return information;
	}

	public void setInformation(AditionalInformation information) {
		this.information = information;
	}

}
