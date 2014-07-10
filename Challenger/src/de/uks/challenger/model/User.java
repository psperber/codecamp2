package de.uks.challenger.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import de.uks.challenger.model.Unit.UNIT_TYPE;

public class User extends ModelElement {
	public static final String PROP_ADD_PROGRESS = "prop_add_progress";
	public static final String PROP_REMOVE_PROGRESS = "prop_remove_progress";
	public static final String PROP_ADD_UNIT = "prop_add_unit";
	public static final String PROP_REMOVE_UNIT = "prop_remove_unit";
	public static final String PROP_SET_WORKOUT_TIME = "prop_set_workout_time";

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
	 * The user prefered resting time in ms beetween worksets 
	 */
	private int restingTime;
	
	/**
	 * Birthday of user
	 */
	private Date birthday;
	private Date workoutTime;

	
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

	public Iterator<Unit> getUnitIteratorByType(UNIT_TYPE type) {
		List<Unit> temp = new ArrayList<Unit>();

		for (Unit unit : getUnits()) {
			if (unit.getUnitType().equals(type)) {
				temp.add(unit);
			}
		}

		return temp.iterator();
	}

	public boolean addUnit(Unit unit) {
		getPropertyChangeSupport()
				.firePropertyChange(PROP_ADD_UNIT, null, unit);
		return getUnits().add(unit);
	}

	public boolean removeUnit(Unit unit) {
		getPropertyChangeSupport().firePropertyChange(PROP_REMOVE_UNIT, null,
				unit);
		return getUnits().remove(unit);
	}

	public Unit getUnit(int index) {
		return getUnits().get(index);
	}

	public Unit removeUnit(int index) {
		Unit unit = getUnits().remove(index);
		getPropertyChangeSupport().firePropertyChange(PROP_REMOVE_UNIT, null,
				unit);

		return unit;
	}

	public int countOfUnits() {
		return this.units == null ? 0 : getUnits().size();
	}

	public Unit getLatestUnit() {
		if (getUnits().size() > 0) {
			return getUnits().get(getUnits().size() - 1);
		} else {
			return null;
		}
	}

	public Unit getLatestUnitByType(UNIT_TYPE type) {
		if (getUnits().size() > 0) {
			for (int i = countOfUnits()-1; i >= 0; --i) {
				Unit unit = getUnit(i);
				if (unit.getUnitType().equals(type)) {
					return unit;
				}
			}

			return null;
		} else {
			return null;
		}

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

		getPropertyChangeSupport().firePropertyChange(PROP_ADD_PROGRESS, null,
				progress);
	}

	public boolean removeProgress(Progress progress) {
		getPropertyChangeSupport().firePropertyChange(PROP_REMOVE_PROGRESS,
				null, progress);
		return getProgress().remove(progress);
	}

	public Progress getProgress(int index) {
		return getProgress().get(index);
	}

	public Progress removeProgress(int index) {
		Progress progress = getProgress().remove(index);
		getPropertyChangeSupport().firePropertyChange(PROP_REMOVE_PROGRESS,
				null, progress);
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
	
	public int getRestingTime() {
		if(restingTime == 0){
			//set one minute as default resting time
			restingTime = 60 * 1000;
		}
		return restingTime;
	}

	public void setRestingTime(int restingTime) {
		this.restingTime = restingTime;
	}


	@Override
	public boolean equals(Object o) {
		if (o == null) {
			return false;
		}

		User other = (User) o;

		if (this.id == other.getId()) {
			return true;
		} else {
			return false;
		}
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}
	
	public Date getWorkoutTime(){
		return this.workoutTime;
	}
	
	public void setWorkoutTime(Date workoutTime){
		Date oldValue = this.workoutTime;
		getPropertyChangeSupport().firePropertyChange(PROP_SET_WORKOUT_TIME, oldValue, workoutTime);
		
		this.workoutTime = workoutTime;
	}

}
