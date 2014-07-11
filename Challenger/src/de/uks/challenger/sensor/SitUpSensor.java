//Sensor for Sit Ups


package de.uks.challenger.sensor;

import de.uks.challenger.model.Unit;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class SitUpSensor extends ChallengerSensor {

	private Sensor sensorAcc;
	private boolean sitUp;

	public SitUpSensor(Context context) {
		super(context);
		this.unitType = Unit.UNIT_TYPE.SIT_UPS;
		this.sensorAcc = getSensorManager().getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		this.sitUp = false;
	}

	public SensorEventListener accListener = new SensorEventListener() {

		public void onAccuracyChanged(Sensor sensor, int acc) {
		}

		public void onSensorChanged(SensorEvent event) {

			float z = event.values[2];
			
			if (!sitUp && z <= -9) {
				sitUp = true;
			}

			if (sitUp && z >= 6) {
				sitUp = false;
				int oldValue = getRepeatCounter();
				setRepeatCounter(oldValue + 1);
				getPropertyChangeSupport().firePropertyChange(PROP_REPEAT,
						oldValue, getRepeatCounter());
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
