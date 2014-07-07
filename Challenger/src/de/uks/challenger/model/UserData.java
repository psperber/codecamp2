package de.uks.challenger.model;

import java.util.Date;

public class UserData {

	/**
	 * The date when the user data was created
	 */
	private Date creationDate;

	/**
	 * Age of the user
	 */
	private int age;

	/**
	 * Height of the user
	 */
	private int height;

	/**
	 * Weight of the user
	 */
	private Double weight;

	/**
	 * Gender of the user
	 */
	private GENDER gender;

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

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public Double getWeight() {
		return weight;
	}

	public void setWeight(Double weight) {
		this.weight = weight;
	}

	public GENDER getGender() {
		return gender;
	}

	public void setGender(GENDER gender) {
		this.gender = gender;
	}

}
