package de.uks.challenger.ui.setup;

import java.util.Date;

import de.uks.challenger.R;
import de.uks.challenger.R.id;
import de.uks.challenger.model.Challenger;
import de.uks.challenger.model.Progress;
import de.uks.challenger.model.User;
import de.uks.challenger.model.User.GENDER;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class Setup1Fragment extends Fragment implements View.OnClickListener {
	EditText mBirthdayEditText;
	Spinner mGenderSpinner;
	EditText mHeightEditText;
	EditText mWeightEditText;
	Button mNextButton;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_setup1, container,
				false);

		mBirthdayEditText = (EditText) rootView
				.findViewById(R.id.birthdayEditText);
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

	@Override
	public void onClick(View v) {
		String birthdayString = mBirthdayEditText.getText().toString();
		if ("".equals(birthdayString)) {
			Toast.makeText(getActivity(), R.string.setup1_error_birthday_empty,
					Toast.LENGTH_SHORT).show();
			return;
		} else {
			System.out.println("birthdayString " + birthdayString);
		}

		String heightString = mHeightEditText.getText().toString();
		if ("".equals(heightString)) {
			Toast.makeText(getActivity(), R.string.setup1_error_height_empty, Toast.LENGTH_SHORT)
					.show();
			return;
		}

		String weightString = mWeightEditText.getText().toString();
		if ("".equals(weightString)) {
			Toast.makeText(getActivity(), R.string.setup1_error_weight_empty, Toast.LENGTH_SHORT)
					.show();
			return;
		}

		int age = Integer.valueOf(birthdayString);
		GENDER gender = mGenderSpinner.getSelectedItemPosition() == 0 ? GENDER.MALE
				: GENDER.FEMALE;
		int height = Integer.valueOf(heightString);
		double weight = Double.valueOf(weightString);

		User user = new User();
		user.setGender(gender);
		user.setHeight(height);

		Progress progress = new Progress();
		progress.setCreationDate(new Date());
		progress.setAge(age);
		progress.setWeight(weight);
		user.addProgress(progress);

		Challenger.getInstance().setUser(user);

		Fragment fragment = Setup2Fragment.newInstance();
		FragmentManager fragmentManager = getFragmentManager();
		fragmentManager.beginTransaction().replace(R.id.container, fragment)
				.commit();
	}

	/**
	 * Returns a new instance of this fragment for the given section number.
	 */
	public static Fragment newInstance() {
		Setup1Fragment fragment = new Setup1Fragment();
		Bundle args = new Bundle();
		// args.putInt(ARG_SECTION_NUMBER, sectionNumber);
		fragment.setArguments(args);
		return fragment;
	}
}
