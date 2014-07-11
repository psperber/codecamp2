package de.uks.challenger.ui.settings;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import twitter4j.auth.AccessToken;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;
import de.uks.challenger.R;
import de.uks.challenger.model.Challenger;
import de.uks.challenger.model.User;
import de.uks.challenger.social.Tweet;
import de.uks.challenger.ui.setup.SetupFragment;

public class SettingsFragment extends Fragment implements View.OnClickListener,
		TimePickerDialog.OnTimeSetListener {
	EditText mWorkoutTimeEditText;
	Button mClearDataButton;
	
	private Tweet tweet = Tweet.getInstance();
	
	Button twitterButton;
	Button twitterSaveButton;
	EditText twitterEditText;
	Challenger challenger = Challenger.getInstance();
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_settings, container,
				false);

		User user = Challenger.getInstance().getUser();

		mWorkoutTimeEditText = (EditText) rootView
				.findViewById(R.id.workouttimeEditText);
		mWorkoutTimeEditText.setOnClickListener(this);
		Date workoutTime = user.getWorkoutTime();
		Calendar c = new GregorianCalendar();
		c.setTime(workoutTime);
		int hourOfDay = c.get(Calendar.HOUR_OF_DAY);
		int minute = c.get(Calendar.MINUTE);
		updateWorkoutTimeEditText(hourOfDay, minute);

		mClearDataButton = (Button) rootView.findViewById(R.id.clearDataButton);
		mClearDataButton.setOnClickListener(this);
		
		twitterButton  = (Button) rootView.findViewById(R.id.twitterButton);
		twitterButton.setOnClickListener(this);

		twitterSaveButton  = (Button) rootView.findViewById(R.id.twitterSaveButton);
		twitterSaveButton.setOnClickListener(this);
		
		twitterEditText = (EditText) rootView
				.findViewById(R.id.twitterEditText);
		
		return rootView;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

	}

	private void updateWorkoutTimeEditText(int hourOfDay, int minute) {
		String msg = hourOfDay < 10 ? "0" : "";
		msg += hourOfDay + ":";
		msg += minute < 10 ? "0" : "";
		msg += minute;
		mWorkoutTimeEditText.setText(msg);
	}

	@Override
	public void onClick(View v) {
		if (v.equals(mClearDataButton)) {
			Challenger.getInstance().setUser(null);

			Fragment fragment = SetupFragment.newInstance();
			FragmentManager fragmentManager = getFragmentManager();
			fragmentManager.beginTransaction()
					.replace(R.id.container, fragment).commit();
		} else if (v.equals(mWorkoutTimeEditText)) {
			String mWorkoutTimeString = mWorkoutTimeEditText.getText()
					.toString();
			String[] mWorkoutTimeSplit = mWorkoutTimeString.split(":");
			int hourOfDay = Integer.valueOf(mWorkoutTimeSplit[0]);
			int minute = Integer.valueOf(mWorkoutTimeSplit[1]);

			TimePickerDialog dialog = new TimePickerDialog(getActivity(), this,
					hourOfDay, minute, true);
			dialog.show();
		} else if (v.equals(twitterButton)) {
			newPin();
		} else if (v.equals(twitterSaveButton)) {
			// Save Twitter
			String pin = twitterEditText.getText().toString();
			if (pin.length() > 0) {
				TaskSaveLogin taskSaveLogin = new TaskSaveLogin(pin);
				taskSaveLogin.execute();
			}
			
			
		}
	}

	@Override
	public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
		User user = Challenger.getInstance().getUser();
		Calendar c = new GregorianCalendar();
		c.set(Calendar.HOUR_OF_DAY, hourOfDay);
		c.set(Calendar.MINUTE, minute);
		user.setWorkoutTime(c.getTime());
		updateWorkoutTimeEditText(hourOfDay, minute);
	}

	/**
	 * Returns a new instance of this fragment for the given section number.
	 */
	public static Fragment newInstance() {
		SettingsFragment fragment = new SettingsFragment();
		Bundle args = new Bundle();
		fragment.setArguments(args);
		return fragment;
	}
	
	
	public void newPin() {
		TaskURL taskURL = new TaskURL();
		taskURL.execute();
	}
	
	
	private class TaskURL extends AsyncTask<Void, Void, String> {
		private String link;

		@Override
		protected String doInBackground(Void... params) {
			link = tweet.newConfig();
			return link;
		}

		@Override
		protected void onPostExecute(String link) {
			Uri uri = Uri.parse(link);
			Intent intent = new Intent(Intent.ACTION_VIEW, uri);
			startActivity(intent);
		}
	}
	
	
	private class TaskSaveLogin extends AsyncTask<Void, Void, Void> {
		@Override
		protected void onPostExecute(Void result) {
			if (success) {
				Toast.makeText(getActivity().getApplicationContext(), "Twitter gespeichert", Toast.LENGTH_SHORT).show();
			}
			super.onPostExecute(result);
		}

		private String pin;
		private boolean success = false;
		
		public TaskSaveLogin(String pin) {
			this.pin = pin;
		}

		@Override
		protected Void doInBackground(Void... params) {
			AccessToken accessToken = tweet.getLogin(pin);

			if (accessToken == null) {
				printErrorToast();
			} else {
				// Save Login
				User user = challenger.getUser();
				user.setSavedAccessToken(accessToken.getToken());
				user.setSavedAccessTokenSecret(accessToken.getTokenSecret());
				success = true;
			}
			return null;
		}
	}
	
	
	private void printErrorToast() {
		getActivity().runOnUiThread(new Runnable() {

			@Override
			public void run() {
				Toast.makeText(
						getActivity().getApplicationContext(),
						"Fehlerhafter Login, bitte in den Einstellungen korrigieren!",
						Toast.LENGTH_SHORT).show();
			}
		});
	}
	
}
