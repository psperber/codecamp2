package de.uks.challenger.ui.setup;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import de.uks.challenger.R;
import de.uks.challenger.model.Challenger;
import de.uks.challenger.model.Progress;
import de.uks.challenger.model.User;
import de.uks.challenger.model.User.GENDER;
import de.uks.challenger.ui.attack.AttackFragment;

/**
 * Sets the new user up
 * 
 * @author Comtec
 *
 */
public class SetupFragment extends Fragment implements View.OnClickListener,
		DatePickerDialog.OnDateSetListener {
	EditText mBirthdayEditText;
	Spinner mGenderSpinner;
	EditText mHeightEditText;
	EditText mWeightEditText;
	Button mNextButton;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_setup, container,
				false);

		mBirthdayEditText = (EditText) rootView
				.findViewById(R.id.birthdayEditText);
		mBirthdayEditText.setOnClickListener(this);
		// init the birthday edittext
		Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR) - 1;
		int month = c.get(Calendar.MONTH);
		int day = c.get(Calendar.DAY_OF_MONTH);
		updateBirthdayEditText(year, month, day);
		mGenderSpinner = (Spinner) rootView.findViewById(R.id.genderSpinner);
		mHeightEditText = (EditText) rootView.findViewById(R.id.heightEditText);
		mWeightEditText = (EditText) rootView.findViewById(R.id.weightEditText);
		mNextButton = (Button) rootView.findViewById(R.id.nextButton);
		mNextButton.setOnClickListener(this);

		return rootView;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

	}
	
	/**
	 * update birthdayEditText
	 * 
	 * @param year
	 * @param month
	 * @param day
	 */
	private void updateBirthdayEditText(int year, int month, int day) {
		String msg = day < 10 ? "0" : "";
		msg += day + ".";
		msg += month < 10 ? "0" : "";
		msg += month + ".";
		msg += year;
		mBirthdayEditText.setText(msg);
	}

	@Override
	public void onClick(View v) {
		if (v.equals(mBirthdayEditText)) {
			// open datepicker if birthdayedittext is clicked
			String birthdayString = mBirthdayEditText.getText().toString();
			String[] birthdaySplit = birthdayString.split("\\.");
			int day = Integer.valueOf(birthdaySplit[0]);
			int month = Integer.valueOf(birthdaySplit[1]);
			int year = Integer.valueOf(birthdaySplit[2]);

			DatePickerDialog dialog = new DatePickerDialog(getActivity(), this,
					year, month, day);
			dialog.getDatePicker().setMaxDate(System.currentTimeMillis() - 1000 * 60 * 60 * 24 * 365);
			dialog.show();
		} else if (v.equals(mNextButton)) {
			// check if fields are filled
			String heightString = mHeightEditText.getText().toString();
			if ("".equals(heightString)) {
				Toast.makeText(getActivity(),
						R.string.setup1_error_height_empty, Toast.LENGTH_SHORT)
						.show();
				return;
			}

			String weightString = mWeightEditText.getText().toString();
			if ("".equals(weightString)) {
				Toast.makeText(getActivity(),
						R.string.setup1_error_weight_empty, Toast.LENGTH_SHORT)
						.show();
				return;
			}
			
			// string to date for birthday
			String birthdayString = mBirthdayEditText.getText().toString();
			String[] birthdaySplit = birthdayString.split("\\.");
			int day = Integer.valueOf(birthdaySplit[0]);
			int month = Integer.valueOf(birthdaySplit[1]);
			int year = Integer.valueOf(birthdaySplit[2]);

			Calendar birthdayCalendar = new GregorianCalendar();
			birthdayCalendar.set(year, month, day);
			Date birthday = birthdayCalendar.getTime();
			GENDER gender = mGenderSpinner.getSelectedItemPosition() == 0 ? GENDER.MALE
					: GENDER.FEMALE;
			int height = Integer.valueOf(heightString);
			double weight = Double.valueOf(weightString);
			
			// default workouttime is 18:00
			Calendar workoutTimeCalendar = new GregorianCalendar();
			workoutTimeCalendar.set(GregorianCalendar.HOUR_OF_DAY, 18);
			workoutTimeCalendar.set(GregorianCalendar.MINUTE, 0);
			workoutTimeCalendar.set(GregorianCalendar.SECOND, 0);
			Date workoutTimeDate = workoutTimeCalendar.getTime();
			
			// setup new user
			User user = new User();
			user.setGender(gender);
			user.setHeight(height);
			user.setBirthday(birthday);
			user.setWorkoutTime(workoutTimeDate);
			
			// add first progress
			Progress progress = new Progress();
			progress.setCreationDate(new Date());
			progress.setAge(user.getAge());
			progress.setWeight(weight);
			user.addProgress(progress);

			Challenger.getInstance().setUser(user);
			
			// start attackfragment for first values
			Fragment fragment = AttackFragment.newInstance();
			FragmentManager fragmentManager = getFragmentManager();
			fragmentManager.beginTransaction()
					.replace(R.id.container, fragment).commit();
		}
	}

	@Override
	public void onDateSet(DatePicker view, int year, int monthOfYear,
			int dayOfMonth) {
		mBirthdayEditText.setText(dayOfMonth + "." + monthOfYear + "." + year);
	}

	/**
	 * Returns a new instance of this fragment for the given section number.
	 */
	public static Fragment newInstance() {
		SetupFragment fragment = new SetupFragment();
		Bundle args = new Bundle();
		// args.putInt(ARG_SECTION_NUMBER, sectionNumber);
		fragment.setArguments(args);
		return fragment;
	}
}
