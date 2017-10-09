package com.techner.tau.server.domain.model;

import java.util.Set;

public class AditionalInformation {
	/** Id */
	private Integer id;
	/** latitude */
	private Double latitude;
	/** altitude */
	private Double altitude;
	/** altitude */
	private Double longitude;
	/** Information of sunset and sunrise **/
	private Set<SunriseSunsetInformation> sunriseSunsetInfo;
	/** Lugar de la ubicación de la estación */
	private String location;

	/**
	 * Constructor
	 */
	public AditionalInformation() {
	}

	/**
	 * @return the latitude
	 */
	public Double getLatitude() {
		return latitude;
	}

	/**
	 * @return the longitude
	 */
	public Double getLongitude() {
		return longitude;
	}

	/**
	 * @param longitude
	 *            the longitude to set
	 */
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	/**
	 * @param latitude
	 *            the latitude to set
	 */
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	/**
	 * @return the altitude
	 */
	public Double getAltitude() {
		return altitude;
	}

	/**
	 * @param altitude
	 *            the altitude to set
	 */
	public void setAltitude(Double altitude) {
		this.altitude = altitude;
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

	/**
	 * @return the sunriseSunsetInfo
	 */
	public Set<SunriseSunsetInformation> getSunriseSunsetInfo() {
		return sunriseSunsetInfo;
	}

	/**
	 * @param sunriseSunsetInfo
	 *            the sunriseSunsetInfo to set
	 */
	public void setSunriseSunsetInfo(Set<SunriseSunsetInformation> sunriseSunsetInfo) {
		this.sunriseSunsetInfo = sunriseSunsetInfo;
	}

	/**
	 * @return the location
	 */
	public String getLocation() {
		return location;
	}

	/**
	 * @param location
	 *            the location to set
	 */
	public void setLocation(String location) {
		this.location = location;
	}

}
