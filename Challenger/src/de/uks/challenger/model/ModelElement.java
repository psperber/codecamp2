package de.uks.challenger.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class ModelElement {
	private PropertyChangeSupport propertyChange = new PropertyChangeSupport(this);
	
	public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener){
		getPropertyChangeSupport().addPropertyChangeListener(propertyName, listener);
	}
	
	public void removePropertyChangeListener(PropertyChangeListener listener){
		getPropertyChangeSupport().removePropertyChangeListener(listener);
	}
	
	protected PropertyChangeSupport getPropertyChangeSupport(){
		if(propertyChange == null){
			propertyChange = new PropertyChangeSupport(this);
		}
		
		return propertyChange;
	}
}
