package com.techner.tau.server.domain.model;

public class Error {

	private Integer id;
	private String description;
	private String codError;

	/**
	 * Default constructor
	 */
	public Error() {
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
	 * @return the codError
	 */
	public String getCodError() {
		return codError;
	}

	/**
	 * @param codError
	 *            the codError to set
	 */
	public void setCodError(String codError) {
		this.codError = codError;
	}
}
