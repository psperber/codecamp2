package de.uks.challenger.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class User {	
	
	/**
	 * History of units
	 */
	private List<Unit> units;
	
	/**
	 * History of user data
	 */
	private List<UserData> userDatas;
	
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
	public List<UserData> getUserDatas(){
		if(userDatas == null){
			userDatas = new ArrayList<UserData>();
		}
		
		return userDatas;
	}

	public void setUnits(List<Unit> units) {
		this.units = units;
	}

	public void setUserDatas(List<UserData> userDatas) {
		this.userDatas = userDatas;
	}
	
	
}
