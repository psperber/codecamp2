package de.uks.challenger.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Unit {
	private long id;
	
	/**
	 * The creation date of the unit
	 */
	public Date creationDate;

	/**
	 * The different sets of the unit
	 */
	private List<Workset> workSets;

	public List<Workset> getWorkSets() {
		if(workSets == null){
			workSets = new ArrayList<Workset>();
		}
		
		return workSets;
	}

	public void setWorkSets(List<Workset> sets) {
		this.workSets = sets;
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
}
