package de.uks.challenger.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class User {
	public enum GENDER {
		MALE, FEMALE;
	}

	private long id;

	/**
	 * History of units
	 */
	private List<Unit> units;

	/**
	 * History of user data
	 */
	private List<Progress> progress;

	/**
	 * Height of the user
	 */
	private int height;

	/**
	 * Gender of the user
	 */
	private GENDER gender;

	/**
	 * Returns the history of units
	 * 
	 * @return
	 */
	private List<Unit> getUnits() {
		if (units == null) {
			units = new ArrayList<Unit>();
		}

		return units;
	}

	public Iterator<Unit> getUnitIterator() {
		return getUnits().iterator();
	}

	public boolean addUnit(Unit unit) {
		return getUnits().add(unit);
	}

	public boolean removeUnit(Unit unit) {
		return getUnits().remove(unit);
	}

	public Unit getUnit(int index) {
		return getUnits().get(index);
	}

	public Unit removeUnit(int index) {
		return getUnits().remove(index);
	}

	/**
	 * Returns the history of user data
	 * 
	 * @return
	 */
	private List<Progress> getProgress() {
		if (progress == null) {
			progress = new ArrayList<Progress>();
		}

		return progress;
	}

	public Iterator<Progress> getProgressIterator() {
		return getProgress().iterator();
	}

	public boolean addProgress(Progress progress) {
		return getProgress().add(progress);
	}

	public boolean removeProgress(Progress Progress) {
		return getProgress().remove(progress);
	}

	public Progress getProgress(int index) {
		return getProgress().get(index);
	}

	public Progress removeProgress(int index) {
		return getProgress().remove(index);
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public GENDER getGender() {
		return gender;
	}

	public void setGender(GENDER gender) {
		this.gender = gender;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

}
