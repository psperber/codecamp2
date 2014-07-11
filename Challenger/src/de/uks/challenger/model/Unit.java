package de.uks.challenger.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class Unit extends ModelElement {
	public enum UNIT_TYPE {
		PUSH_UPS("Push Up"), SIT_UPS("Sit Up"), JUMPING_JACK("Jumping Jack");
		private String s;
		
		private UNIT_TYPE(String s) {
			this.s = s;
		}
		
		@Override
		public String toString() {
			return s;
		}
	}

	private long id;

	/**
	 * The creation date of the unit
	 */
	private Date creationDate;

	/**
	 * The different sets of the unit
	 */
	private List<Workset> workSets;

	/**
	 * resting time in ms
	 */
	private int restingTime;

	/**
	 * Unit type of the unit
	 */
	private UNIT_TYPE unitType;
	
	public int getRestingTime() {
		return restingTime;
	}

	public void setRestingTime(int restingTime) {
		this.restingTime = restingTime;
	}

	public List<Workset> getWorkSets() {
		if (workSets == null) {
			workSets = new ArrayList<Workset>();
		}

		return workSets;
	}

	public Iterator<Workset> getWorksetIterator() {
		return getWorkSets().iterator();
	}

	public boolean addWorkset(Workset workset) {
		return getWorkSets().add(workset);
	}

	public boolean removeWorkset(Workset workset) {
		return getWorkSets().remove(workset);
	}

	public Workset getWorkset(int index) {
		return getWorkSets().get(index);
	}

	public Workset removeWorkset(int index) {
		return getWorkSets().remove(index);
	}
	
	public int countOfWorksets(){
		return getWorkSets().size();
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public UNIT_TYPE getUnitType() {
		return unitType;
	}

	public void setUnitType(UNIT_TYPE unitType) {
		this.unitType = unitType;
	}
}
