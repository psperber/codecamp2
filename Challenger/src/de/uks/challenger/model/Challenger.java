package de.uks.challenger.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;


public class Challenger extends ModelElement{
	public static final String PROP_SET_USER = "prop_set_user";
	
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
		
		User oldUser = this.user;		
		this.user = user;
		
		getPropertyChangeSupport().firePropertyChange(PROP_SET_USER, oldUser, this.user);
	}
	
}
