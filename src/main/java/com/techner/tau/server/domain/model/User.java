package com.techner.tau.server.domain.model;

public class User {

	private Integer id;
	private String name;
	private String lastName;
	private String email;
	private SimCard simCard;
	private boolean active;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public SimCard getSimCard() {
		return simCard;
	}

	public void setSimCard(SimCard simCard) {
		this.simCard = simCard;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}
}
