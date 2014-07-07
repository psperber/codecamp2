package de.uks.challenger.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Unit {
	
	/**
	 * The creation date of the unit
	 */
	public Date creationDate;

	/**
	 * The different sets of the unit
	 */
	private List<WorkSet> workSets;

	public List<WorkSet> getWorkSets() {
		if(workSets == null){
			return new ArrayList<WorkSet>();
		}
		
		return workSets;
	}

	public void setWorkSets(List<WorkSet> sets) {
		this.workSets = sets;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
}
