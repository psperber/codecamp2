package de.uks.challenger.model;



public class Challenger extends ModelElement{
	public static final String PROP_SET_USER = "prop_set_user";
	public static final int PROP_COUNT_WORKINGSETS = 3;
	
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
	

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		
		User oldUser = this.user;		
		this.user = user;
		
		getPropertyChangeSupport().firePropertyChange(PROP_SET_USER, oldUser, this.user);
	}
	
}
