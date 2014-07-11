//Super class for sensors

package de.uks.challenger.sensor;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Date;

import android.content.Context;
import android.hardware.SensorManager;
import de.uks.challenger.model.Challenger;
import de.uks.challenger.model.Unit;
import de.uks.challenger.model.Unit.UNIT_TYPE;

public abstract class ChallengerSensor {

	private SensorManager sensorManager;
	private Context context;
	public static final String PROP_REPEAT = "prop_repeat";
	private int repeatCounter = 0;
	private int worksetCounter = 0;
	protected UNIT_TYPE unitType;

	protected Unit unit;
	protected Unit latestUnit;

	public ChallengerSensor(Context context) {
		this.context = context;
		this.sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);

	}

	
	public UNIT_TYPE getUnitType() {
		return unitType;
	}
	
	
	
	public int getWorksetCounter() {
		return worksetCounter;
	}

	public void setWorksetCounter(int worksetCounter) {
		this.worksetCounter = worksetCounter;
	}

	public abstract void start();

	public abstract void stop();

	public Unit getUnit() {
		return unit;
	}

	protected Context getContext() {
		return context;
	}

	protected SensorManager getSensorManager() {
		return sensorManager;
	}

	private PropertyChangeSupport propertyChange = new PropertyChangeSupport(this);

	public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
		getPropertyChangeSupport().addPropertyChangeListener(propertyName, listener);
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		getPropertyChangeSupport().removePropertyChangeListener(listener);
	}

	protected PropertyChangeSupport getPropertyChangeSupport() {
		if (propertyChange == null) {
			propertyChange = new PropertyChangeSupport(this);
		}

		return propertyChange;
	}

	public int getRepeatCounter() {
		return repeatCounter;
	}

	public void setRepeatCounter(int repeatCounter) {
		this.repeatCounter = repeatCounter;
	}

	public void doNext() {
		// update values
		this.worksetCounter++;
		this.repeatCounter = 0;

	}

	public Unit getLatestUnit() {
		return this.latestUnit;
	}

	public void pushModel() {
		
		unit.setCreationDate(new Date());
		Challenger.getInstance().getUser().addUnit(this.unit);

	}

}
