package de.uks.challenger.model;

import java.util.Date;

public class Progress {
	private long id;

	/**
	 * The date when the user data was created
	 */
	private Date creationDate;

	/**
	 * Age of the user
	 */
	private int age;

	
	/**
	 * Weight of the user
	 */
	private Double weight;

	

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}



	public Double getWeight() {
		return weight;
	}

	public void setWeight(Double weight) {
		this.weight = weight;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

}
