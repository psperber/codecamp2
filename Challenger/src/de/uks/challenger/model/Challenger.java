package de.uks.challenger.model;

import de.uks.challenger.persistence.UnitSource;

public class Challenger {
	
	private static Challenger instance;

	/**
	 * The user of challenger
	 */
	private User user;
	
	/**
	 * Returns the singleton instance of the modle
	 * @return
	 */
	public static Challenger getInstance(){
		if(instance == null){
			instance = new Challenger();
		}
		
		return instance;
	}
	
	/**
	 * Returns the next unit for the user to do
	 * @return
	 */
	public Unit getNextUnit(){
		//theoretisch ist durch eingangstest immer eine erste unit da
		
		//wenn keine units unit mit default wert
		
		//sonst letzt unit holen, anzahl der wiederholungnen + 1
		
		return null;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}
