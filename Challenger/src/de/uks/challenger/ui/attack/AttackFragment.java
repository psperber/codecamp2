package de.uks.challenger.ui.attack;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Date;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import de.uks.challenger.R;
import de.uks.challenger.model.Challenger;
import de.uks.challenger.model.Unit;
import de.uks.challenger.model.Workset;
import de.uks.challenger.sensor.ChallengerSensor;
import de.uks.challenger.sensor.JumpingJackSensor;
import de.uks.challenger.sensor.PushUpSensor;
import de.uks.challenger.sensor.SitUpSensor;
import de.uks.challenger.ui.MainActivity;

public class AttackFragment extends Fragment {
	// in seconds
	private static final int PAUSETIME = 60;// 60;
	public static final int COUNT_WORKINGSETS = 3;

	Challenger challenger = Challenger.getInstance();

	ChallengerSensor challengerSensor;
	TextView currentCountTextView;
	TextView overAllTextView;
	TextView setXTextView;
	TextView unitTypeTextView;

	Button startButton;
	Button nextButton;
	Unit latestUnit;
	Unit newUnit;

	ArrayList<ChallengerSensor> sensors;
	int sensorCount = 0;
	int worksetCount = 0;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_attack, container,
				false);
		this.currentCountTextView = (TextView) rootView
				.findViewById(R.id.currentCountTextView);
		this.overAllTextView = (TextView) rootView
				.findViewById(R.id.overallCountTextView);
		this.setXTextView = (TextView) rootView.findViewById(R.id.setTextView);
		this.unitTypeTextView = (TextView) rootView
				.findViewById(R.id.unittypeTextView);
		this.startButton = (Button) rootView.findViewById(R.id.startButton);
		this.nextButton = (Button) rootView.findViewById(R.id.nextRoundButton);
		this.nextButton.setEnabled(false);
		this.sensors = new ArrayList<ChallengerSensor>();
		this.sensors
				.add(new PushUpSensor(getActivity().getApplicationContext()));
		this.sensors
				.add(new SitUpSensor(getActivity().getApplicationContext()));
		this.sensors.add(new JumpingJackSensor(getActivity()
				.getApplicationContext()));

		int countOfUnits = challenger.getUser().countOfUnits();
		if (countOfUnits == 0) {
			// first app run
			this.latestUnit = null;
		} else {
			this.latestUnit = challenger.getUser().getLatestUnitByType(
					sensors.get(sensorCount).getUnitType());
		}

		this.newUnit = generateNewUnit();

		overAllTextView
				.setText(newUnit.getWorkset(worksetCount).getTodo() + "");
		setXTextView.setText(worksetCount + 1 + "");
		unitTypeTextView.setText(newUnit.getUnitType().toString());

		startButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				challengerSensor.start();
				startButton.setEnabled(false);
				nextButton.setEnabled(true);

			}
		});

		nextButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				int counter = challengerSensor.getRepeatCounter();
				newUnit.getWorkset(worksetCount).setCount(counter);
				newUnit.getWorkset(worksetCount).setTodo(counter++);
				worksetCount++;
				setXTextView.setText(worksetCount + 1 + "");
				unitTypeTextView.setText(newUnit.getUnitType().toString());
				challengerSensor.setRepeatCounter(0);
				currentCountTextView.setText(challengerSensor
						.getRepeatCounter() + "");

				// unit finished
				if (worksetCount == COUNT_WORKINGSETS) {
					challenger.getUser().addUnit(newUnit);
					sensorCount++;
					worksetCount = 0;

					if (sensorCount < sensors.size()) {
						
						challengerSensor.stop();
						new PauseTask().execute();
						challengerSensor = sensors.get(sensorCount);
						challengerSensor.addPropertyChangeListener(
								ChallengerSensor.PROP_REPEAT,
								new RepeatListener());
						challengerSensor.start();
					} else {
						MainActivity activity = (MainActivity) getActivity();
						activity.selectItem(0);
						return;

					}

					latestUnit = challenger.getUser().getLatestUnitByType(
							challengerSensor.getUnitType());
					newUnit = generateNewUnit();
					unitTypeTextView.setText(newUnit.getUnitType().toString());
					setXTextView.setText(worksetCount + 1 + "");
					overAllTextView.setText(newUnit.getWorkset(worksetCount)
							.getTodo() + "");
					currentCountTextView.setText("0");

				} else {
					new PauseTask().execute();
				}

			}
		});

		challengerSensor = sensors.get(0);

		challengerSensor.addPropertyChangeListener(
				ChallengerSensor.PROP_REPEAT, new RepeatListener());

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
	
	@Override
	public void onDetach() {
		super.onDetach();
		challengerSensor.stop();
	}

	//refresh the ui with new fired count value from a sensor
	private class RepeatListener implements PropertyChangeListener {

		@Override
		public void propertyChange(final PropertyChangeEvent event) {
			Activity activity = getActivity();
			activity.runOnUiThread(new Runnable() {

				@Override
				public void run() {
					currentCountTextView.setText(event.getNewValue() + "");

				}
			});

		}

	}

	//pause between workingsets and units
	private class PauseTask extends AsyncTask<Void, Void, Void> {
		ProgressDialog asycDialog;

		@Override
		protected void onPreExecute() {
			asycDialog = new ProgressDialog(getActivity());
			// set message of the dialog
			asycDialog.setMessage(getString(R.string.attack_pause_message));
			// show dialog
			asycDialog.show();
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			try {
				Thread.sleep(1000 * PAUSETIME);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// don't touch dialog here it'll break the application
			// do some lengthy stuff like calling login webservice

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// hide the dialog
			asycDialog.dismiss();
			super.onPostExecute(result);
		}

	}
}
