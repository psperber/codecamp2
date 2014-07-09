package de.uks.challenger.model;

import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class User extends ModelElement{
	public static final String PROP_ADD_PROGRESS = "prop_add_progress";
	public static final String PROP_REMOVE_PROGRESS = "prop_remove_progress";
	public static final String PROP_ADD_UNIT = "prop_add_unit";
	public static final String PROP_REMOVE_UNIT = "prop_remove_unit";
	
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
		getPropertyChangeSupport().firePropertyChange(PROP_ADD_UNIT, null, unit);
		return getUnits().add(unit);
	}

	public boolean removeUnit(Unit unit) {
		getPropertyChangeSupport().firePropertyChange(PROP_REMOVE_UNIT, null, unit);
		return getUnits().remove(unit);
	}

	public Unit getUnit(int index) {
		return getUnits().get(index);
	}

	public Unit removeUnit(int index) {
		Unit unit = getUnits().remove(index);
		getPropertyChangeSupport().firePropertyChange(PROP_REMOVE_UNIT, null, unit);
		
		return unit;
	}

	public int countOfUnits() {
		return this.units == null ? 0 : getUnits().size();
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

	public void addProgress(Progress progress) {
		int index = 0;
		for (int i = 0; i < countOfProgress(); i++) {
			Progress tmpProgress = getProgress(i);
			if (progress.getCreationDate().after(tmpProgress.getCreationDate())) {
				index = i;
			}
		}
		getProgress().add(index, progress);
		
		getPropertyChangeSupport().firePropertyChange(PROP_ADD_PROGRESS, null, progress);
	}

	public boolean removeProgress(Progress progress) {
		getPropertyChangeSupport().firePropertyChange(PROP_REMOVE_PROGRESS, null, progress);
		return getProgress().remove(progress);
	}

	public Progress getProgress(int index) {
		return getProgress().get(index);
	}

	public Progress removeProgress(int index) {
		Progress progress = getProgress().remove(index);
		getPropertyChangeSupport().firePropertyChange(PROP_REMOVE_PROGRESS, null, progress);
		return progress;
	}

	public int countOfProgress() {
		return this.progress == null ? 0 : getProgress().size();
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
	
	@Override
	public boolean equals(Object o) {
		if(o == null){
			return false;
		}
		
		User other = (User) o;
		
		if(this.id == other.getId()){
			return true;
		}else{
			return false;
		}
	}

}
