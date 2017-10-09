package com.techner.tau.server.domain.model;

import java.util.Date;

/**
 * Alertas por estaciones
 * 
 * @author juan
 * 
 */
public class AlertRule {
	/** id */
	private Integer id;
	/** alerta */
	private Alert alert;
	/** estacion */
	private Station station;
	/** fecha alta */
	private Date startDate;
	/** fecha baja */
	private Date endDate;
	/** umbral de minima */
	private Double min;
	/** umbral de máxima */
	private Double max;
	/** Notificaciones por sms habilitadas o no */
	private boolean smsEnable;
	/** Notificaciones por email habilitadas o no */
	private boolean emailEnable;

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
	 * @return the alert
	 */
	public Alert getAlert() {
		return alert;
	}

	/**
	 * @param alert
	 *            the alert to set
	 */
	public void setAlert(Alert alert) {
		this.alert = alert;
	}

	/**
	 * @return the station
	 */
	public Station getStation() {
		return station;
	}

	/**
	 * @param station
	 *            the station to set
	 */
	public void setStation(Station station) {
		this.station = station;
	}

	/**
	 * @return the startDate
	 */
	public Date getStartDate() {
		return startDate;
	}

	/**
	 * @param startDate
	 *            the startDate to set
	 */
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	/**
	 * @return the endDate
	 */
	public Date getEndDate() {
		return endDate;
	}

	/**
	 * @param endDate
	 *            the endDate to set
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	/**
	 * @return the min
	 */
	public Double getMin() {
		return min;
	}

	/**
	 * @param min
	 *            the min to set
	 */
	public void setMin(Double min) {
		this.min = min;
	}

	/**
	 * @return the max
	 */
	public Double getMax() {
		return max;
	}

	/**
	 * @param max
	 *            the max to set
	 */
	public void setMax(Double max) {
		this.max = max;
	}

	/**
	 * @return the smsEnable
	 */
	public boolean isSmsEnable() {
		return smsEnable;
	}

	/**
	 * @param smsEnable
	 *            the smsEnable to set
	 */
	public void setSmsEnable(boolean smsEnable) {
		this.smsEnable = smsEnable;
	}

	/**
	 * @return the emailEnable
	 */
	public boolean isEmailEnable() {
		return emailEnable;
	}

	/**
	 * @param emailEnable
	 *            the emailEnable to set
	 */
	public void setEmailEnable(boolean emailEnable) {
		this.emailEnable = emailEnable;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "AlertRule [id=" + id + ", alert=" + alert + ", min=" + min + ", max=" + max + "]";
	}
	
	

}
