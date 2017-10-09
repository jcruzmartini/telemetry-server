package com.techner.tau.server.domain.model;

import java.util.Date;

public class PeriodicalNotification {

	/** id */
	private Integer id;
	/** habilitado lunes p notificaciones */
	private boolean monday;
	/** habilitado martes p notificaciones */
	private boolean tuesday;
	/** habilitado miercoles p notificaciones */
	private boolean wednesday;
	/** habilitado jueves p notificaciones */
	private boolean thursday;
	/** habilitado viernes p notificaciones */
	private boolean friday;
	/** habilitado sabado p notificaciones */
	private boolean saturday;
	/** habilitado domingo p notificaciones */
	private boolean sunday;
	/** Hora de notificacion */
	private Date hour;
	/** Estacion involucrada */
	private Station station;
	/** Notificaciones por sms habilitadas o no */
	private boolean smsEnable;
	/** Notificaciones por email habilitadas o no */
	private boolean emailEnable;

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
	 * @return the monday
	 */
	public boolean isMonday() {
		return monday;
	}

	/**
	 * @param monday
	 *            the monday to set
	 */
	public void setMonday(boolean monday) {
		this.monday = monday;
	}

	/**
	 * @return the tuesday
	 */
	public boolean isTuesday() {
		return tuesday;
	}

	/**
	 * @param tuesday
	 *            the tuesday to set
	 */
	public void setTuesday(boolean tuesday) {
		this.tuesday = tuesday;
	}

	/**
	 * @return the wednesday
	 */
	public boolean isWednesday() {
		return wednesday;
	}

	/**
	 * @param wednesday
	 *            the wednesday to set
	 */
	public void setWednesday(boolean wednesday) {
		this.wednesday = wednesday;
	}

	/**
	 * @return the thursday
	 */
	public boolean isThursday() {
		return thursday;
	}

	/**
	 * @param thursday
	 *            the thursday to set
	 */
	public void setThursday(boolean thursday) {
		this.thursday = thursday;
	}

	/**
	 * @return the friday
	 */
	public boolean isFriday() {
		return friday;
	}

	/**
	 * @param friday
	 *            the friday to set
	 */
	public void setFriday(boolean friday) {
		this.friday = friday;
	}

	/**
	 * @return the saturday
	 */
	public boolean isSaturday() {
		return saturday;
	}

	/**
	 * @param saturday
	 *            the saturday to set
	 */
	public void setSaturday(boolean saturday) {
		this.saturday = saturday;
	}

	/**
	 * @return the sunday
	 */
	public boolean isSunday() {
		return sunday;
	}

	/**
	 * @param sunday
	 *            the sunday to set
	 */
	public void setSunday(boolean sunday) {
		this.sunday = sunday;
	}

	/**
	 * @return the hour
	 */
	public Date getHour() {
		return hour;
	}

	/**
	 * @param hour
	 *            the hour to set
	 */
	public void setHour(Date hour) {
		this.hour = hour;
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

}
