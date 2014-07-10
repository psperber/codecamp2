package de.uks.challenger.sensor;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import de.uks.challenger.model.Unit;


public class PushUpSensor extends ChallengerSensor {

	private Sensor sensorLight;
	private boolean pushUp;
	
	
	
	public PushUpSensor(Context context) {
		super(context);
		this.unitType = Unit.UNIT_TYPE.PUSH_UPS;
		this.sensorLight = getSensorManager().getDefaultSensor(Sensor.TYPE_LIGHT);
		this.pushUp = false;
	}
	


	public SensorEventListener lightListener = new SensorEventListener() {
		public void onAccuracyChanged(Sensor sensor, int acc) {
		}

		public void onSensorChanged(SensorEvent event) {

			float x = event.values[0];

			// System.out.println("lux: " + (int)x);

			if ((int) x <= 15 && !PushUpSensor.this.pushUp) {
				int oldValue = getRepeatCounter();
				setRepeatCounter(oldValue + 1);
				getPropertyChangeSupport().firePropertyChange(PROP_REPEAT, oldValue, getRepeatCounter());
				PushUpSensor.this.pushUp = true;
			}

			if ((int) x > 15 && PushUpSensor.this.pushUp) {
				PushUpSensor.this.pushUp = false;
			}

		}
	};

	@Override
	public void start() {
		getSensorManager().registerListener(lightListener, sensorLight, SensorManager.SENSOR_DELAY_NORMAL);

		// Test by philipp, da kein helligkeitssensor
		/*Timer timer = new Timer();
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				int oldValue = getCounter();
				setCounter(oldValue + 1);
				getPropertyChangeSupport().firePropertyChange(PROP_REPEAT, oldValue, getCounter());

			}
		}, 0, 1000);*/
		//Endtest
	}

	@Override
	public void stop() {
		getSensorManager().unregisterListener(lightListener);
	}


}
