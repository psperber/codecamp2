package de.uks.challenger.ui.settings;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import de.uks.challenger.R;
import de.uks.challenger.R.layout;
import de.uks.challenger.model.Challenger;
import de.uks.challenger.model.User;
import de.uks.challenger.ui.attack.AttackFragment;
import de.uks.challenger.ui.setup.SetupFragment;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.app.TimePickerDialog;

public class SettingsFragment extends Fragment implements View.OnClickListener,
		TimePickerDialog.OnTimeSetListener {
	EditText mWorkoutTimeEditText;
	Button mClearDataButton;

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
		// args.putInt(ARG_SECTION_NUMBER, sectionNumber);
		fragment.setArguments(args);
		return fragment;
	}
}
