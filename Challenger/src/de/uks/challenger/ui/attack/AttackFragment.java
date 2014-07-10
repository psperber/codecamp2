package de.uks.challenger.ui.attack;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Date;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import de.uks.challenger.R;
import de.uks.challenger.model.Challenger;
import de.uks.challenger.model.Unit;
import de.uks.challenger.model.Workset;
import de.uks.challenger.sensor.ChallengerSensor;
import de.uks.challenger.sensor.JumpingJackSensor;
import de.uks.challenger.sensor.PushUpSensor;
import de.uks.challenger.sensor.SitUpSensor;



public class AttackFragment extends Fragment {

	public static final int COUNT_WORKINGSETS = 3;
	
	Challenger challenger = Challenger.getInstance();
	
	ChallengerSensor challengerSensor;
	TextView currentCountTextView;
	TextView todoTextView;
	Button startButton;
	Button nextButton;
	Unit latestUnit;
	Unit newUnit;
	

	ArrayList<ChallengerSensor> sensors;
	int sensorCount = 0;
	int worksetCount = 0;
	
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_attack, container, false);
		this.currentCountTextView = (TextView) rootView.findViewById(R.id.currentCountTextView);
		this.todoTextView = (TextView) rootView.findViewById(R.id.todoTextView);
		this.startButton = (Button) rootView.findViewById(R.id.startButton);
		this.nextButton = (Button) rootView.findViewById(R.id.nextRoundButton);
		this.sensors = new ArrayList<ChallengerSensor>();
		this.sensors.add(new PushUpSensor(getActivity().getApplicationContext()));
		this.sensors.add(new SitUpSensor(getActivity().getApplicationContext()));
		this.sensors.add(new JumpingJackSensor(getActivity().getApplicationContext()));

		int countOfUnits = challenger.getUser().countOfUnits();
		if (countOfUnits == 0) {
			//App wird zum ersten mal gestartet
			this.latestUnit = null;
		} else {
			this.latestUnit = challenger.getUser().getLatestUnitByType(sensors.get(sensorCount).getUnitType());		
		}

		this.newUnit = generateNewUnit();

		
		todoTextView.setText(newUnit.getWorkset(worksetCount).getTodo() + "");			
		

		startButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				challengerSensor.start();

			}
		});

		nextButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				int counter = challengerSensor.getRepeatCounter();
				newUnit.getWorkset(worksetCount).setCount(counter);
				newUnit.getWorkset(worksetCount).setTodo(counter++);
				worksetCount++;
				//unit ist abgeschlossen
				if (worksetCount == COUNT_WORKINGSETS) {
					challenger.getUser().addUnit(newUnit);
					sensorCount++; 
					
					
					if (sensorCount < sensors.size()) {
						challengerSensor.stop();
						challengerSensor = sensors.get(sensorCount);
						challengerSensor.start();
					} else {
						// eier schaukeln und rausgehen.... TODO in history fragment
						Toast.makeText(getActivity(), "Finished Workout", Toast.LENGTH_SHORT).show();
					}
					
					latestUnit = challenger.getUser().getLatestUnitByType(challengerSensor.getUnitType());
					newUnit = generateNewUnit();
					todoTextView.setText(newUnit.getWorkset(worksetCount).getTodo() + "");
					currentCountTextView.setText("0");

				}
				
				//PAUSE von 60sec oder so
				
			}
		});

		challengerSensor = sensors.get(0);

		challengerSensor.addPropertyChangeListener(ChallengerSensor.PROP_REPEAT, new RepeatListener());
		
		return rootView;
	}

	private Unit generateNewUnit() {
		Unit generateUnit = new Unit();
		for (int i = 0; i < COUNT_WORKINGSETS; i++) {
			Workset workset = new Workset();
			if (this.latestUnit != null) {
				workset.setTodo(this.latestUnit.getWorkset(i).getCount() + 1);
			} else {
				workset.setTodo(0);
			}
			
			generateUnit.addWorkset(workset);
			
		}
		generateUnit.setUnitType(sensors.get(sensorCount).getUnitType());
		generateUnit.setCreationDate(new Date());
		
		return generateUnit;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	/**
	 * Returns a new instance of this fragment for the given section number.
	 */
	public static Fragment newInstance() {
		AttackFragment fragment = new AttackFragment();
		Bundle args = new Bundle();
		// args.putInt(ARG_SECTION_NUMBER, sectionNumber);
		fragment.setArguments(args);
		return fragment;
	}

	private class RepeatListener implements PropertyChangeListener {

		@Override
		public void propertyChange(final PropertyChangeEvent event) {
			getActivity().runOnUiThread(new Runnable() {

				@Override
				public void run() {
					currentCountTextView.setText(event.getNewValue() + "");

				}
			});

		}

	}
}
