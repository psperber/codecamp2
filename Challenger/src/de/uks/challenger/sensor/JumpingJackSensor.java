package de.uks.challenger.sensor;

import de.uks.challenger.model.Unit;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;


public class JumpingJackSensor extends ChallengerSensor {

	private Sensor sensorAcc;

	
	
	 float appliedAcceleration = 0;
	 float currentAcceleration = 0;
	 float velocity = 0;
	//Date lastUpdate;
	 
	 Handler handler = new Handler();
	 
	 private long lastUpdate = 0;
	 private float last_x = 0;
	 private float last_y = 0;
	 private float last_z = 0;
	 private static final int SHAKE_THRESHOLD = 600;
	 private long lastShake = 0;

	public JumpingJackSensor(Context context) {
		super(context);
		this.unitType = Unit.UNIT_TYPE.JUMPING_JACK;
		this.sensorAcc = getSensorManager().getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

	}

	public SensorEventListener accListener = new SensorEventListener() {

		public void onAccuracyChanged(Sensor sensor, int acc) {
		}

		public void onSensorChanged(SensorEvent event) {
			double calibration = Double.NaN;
			
		
	 
				float x=event.values[0];
				float y=event.values[1];
				float z=event.values[2];

				
				
				
				long curTime = System.currentTimeMillis();
				 
		        if ((curTime - lastUpdate) > 100) {
		            long diffTime = (curTime - lastUpdate);
		            lastUpdate = curTime;
		            long diffShake = (curTime - lastShake);
		       	 
		 
		            float speed = Math.abs(x + y + z - last_x - last_y - last_z)/ diffTime * 10000;
		        
		            
		            if (speed > SHAKE_THRESHOLD && diffShake > 10000) {
						int oldValue = getRepeatCounter();
						setRepeatCounter(oldValue + 1);
		            	getPropertyChangeSupport().firePropertyChange(PROP_REPEAT,
								oldValue, getRepeatCounter());
		            	lastShake = curTime;
		            	
		            }
		            
		            last_x = x;
		            last_y = y;
		            last_z = z;
		        }
				
		}

	};

	@Override
	public void start() {
		getSensorManager().registerListener(accListener, sensorAcc,
				SensorManager.SENSOR_DELAY_UI);
	}

	@Override
	public void stop() {
		getSensorManager().unregisterListener(accListener);
	}

}
