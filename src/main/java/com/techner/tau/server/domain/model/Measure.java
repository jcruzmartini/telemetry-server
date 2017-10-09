package com.techner.tau.server.domain.model;

import java.util.Date;

public class Measure {

	private Integer id;
	private Variable variable;
	private Double value;
	private Date date;
	private Station station;
	private boolean calculated;

	/**
	 * Default constructor
	 */
	public Measure() {

	}

	/**
	 * Constructor
	 * 
	 * @param variable
	 *            variable
	 * @param value
	 *            valor
	 * @param date
	 *            fecha
	 * @param station
	 *            estacion
	 * @param calculated
	 *            true si es calculado, false si no
	 */
	public Measure(Variable variable, Double value, Date date, Station station, boolean calculated) {
		this.variable = variable;
		this.value = value;
		this.date = date;
		this.station = station;
		this.calculated = calculated;
	}

	/**
	 * @return
	 */
	public Double getValue() {
		return value;
	}

	/**
	 * @param value
	 */
	public void setValue(Double value) {
		this.value = value;
	}

	/**
	 * @return
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * @param date
	 */
	public void setDate(Date date) {
		this.date = date;
	}

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
	public Variable getVariable() {
		return variable;
	}

	/**
	 * @param variable
	 */
	public void setVariable(Variable variable) {
		this.variable = variable;
	}

	public Station getStation() {
		return station;
	}

	public void setStation(Station station) {
		this.station = station;
	}

	public boolean isCalculated() {
		return calculated;
	}

	public void setCalculated(boolean calculated) {
		this.calculated = calculated;
	}

}
