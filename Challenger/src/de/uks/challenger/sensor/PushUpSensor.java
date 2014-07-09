package de.uks.challenger.sensor;


import de.uks.challenger.model.Challenger;
import de.uks.challenger.model.Unit;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.widget.Toast;

public class PushUpSensor extends ChallengerSensor {

	private Sensor sensorLight;
	private boolean pushUp;
	 
	
	public PushUpSensor(Context context) {
		super(context);
		this.sensorLight = getSensorManager().getDefaultSensor(Sensor.TYPE_LIGHT);
		this.pushUp= false; 
		this.unit = new Unit();
		this.unit.setUnitType(Unit.UNIT_TYPE.PUSH_UPS);
		this.latestUnit = Challenger.getInstance().getUser().getLatestUnitByType(this.unit.getUnitType());
	}
	
	
	public SensorEventListener lightListener = new SensorEventListener() {
		public void onAccuracyChanged(Sensor sensor, int acc) { }
 
		public void onSensorChanged(SensorEvent event) {

			
			float x = event.values[0];

			//System.out.println("lux: " + (int)x);
			
			if ((int)x <= 15 && !PushUpSensor.this.pushUp) {
				int oldValue = getCounter();
				setCounter(oldValue + 1);
				getPropertyChangeSupport().firePropertyChange(PROP_REPEAT, oldValue, getCounter());
				PushUpSensor.this.pushUp = true;
			}
			
			if ((int)x > 15 && PushUpSensor.this.pushUp) {
				PushUpSensor.this.pushUp = false;
			}
		
		}
	};

	@Override
	public void start() {
		 getSensorManager().registerListener (lightListener, sensorLight,
	                SensorManager.SENSOR_DELAY_NORMAL);
	}

	@Override
	public void stop() {
		getSensorManager().unregisterListener (lightListener);
	}
	
	
	
	
	

}
