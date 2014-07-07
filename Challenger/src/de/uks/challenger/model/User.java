package de.uks.challenger.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class User {
	private long id;
	
	/**
	 * History of units
	 */
	private List<Unit> units;
	
	/**
	 * History of user data
	 */
	private List<Progress> userDatas;
	
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
	 * @return
	 */
	public List<Unit> getUnits(){
		if(units == null){
			units = new ArrayList<Unit>();
		}
		
		return units;
	}
	
	/**
	 * Returns the history of user data
	 * @return
	 */
	public List<Progress> getUserDatas(){
		if(userDatas == null){
			userDatas = new ArrayList<Progress>();
		}
		
		return userDatas;
	}

	public void setUnits(List<Unit> units) {
		this.units = units;
	}

	public void setUserDatas(List<Progress> userDatas) {
		this.userDatas = userDatas;
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
