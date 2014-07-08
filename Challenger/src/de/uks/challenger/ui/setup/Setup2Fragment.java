package de.uks.challenger.ui.setup;

import java.util.Date;

import de.uks.challenger.R;
import de.uks.challenger.R.id;
import de.uks.challenger.model.Challenger;
import de.uks.challenger.model.Unit;
import de.uks.challenger.model.Unit.UNIT_TYPE;
import de.uks.challenger.model.User;
import de.uks.challenger.model.Workset;
import de.uks.challenger.ui.MainActivity;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Setup2Fragment extends Fragment implements View.OnClickListener {
	EditText mCountEditText;
	EditText mTimeEditText;
	Button mSkipButton;
	Button mFinishButton;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_setup2, container,
				false);

		mCountEditText = (EditText) rootView.findViewById(R.id.countEditText);
		mTimeEditText = (EditText) rootView.findViewById(R.id.timeEditText);
		mSkipButton = (Button) rootView.findViewById(R.id.skipButton);
		mSkipButton.setOnClickListener(this);
		mFinishButton = (Button) rootView.findViewById(R.id.finishButton);
		mFinishButton.setOnClickListener(this);

		return rootView;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

	}

	@Override
	public void onClick(View v) {
		if (v.equals(mSkipButton)) {

		} else if (v.equals(mFinishButton)) {
			String countString = mCountEditText.getText().toString();
			if ("".equals(countString)) {
				Toast.makeText(getActivity(), "COUNT IS EMPTY",
						Toast.LENGTH_SHORT).show();
				return;
			}

			String timeString = mTimeEditText.getText().toString();
			if ("".equals(timeString)) {
				Toast.makeText(getActivity(), "TIME IS EMPTY",
						Toast.LENGTH_SHORT).show();
				return;
			}
			
			int count = Integer.valueOf(countString);
			int time = Integer.valueOf(timeString);
			
			Unit unit = new Unit();
			unit.setCreationDate(new Date());
			unit.setRestingTime(0);
			unit.setUnitType(UNIT_TYPE.PUSH_UPS);
			
			Workset workset = new Workset();
			workset.setTodo(count);
			workset.setCount(count);
			// TODO time
			unit.addWorkset(workset);
			
			User user = Challenger.getInstance().getUser();
			user.addUnit(unit);
		}

		MainActivity mainActivity = (MainActivity) getActivity();
		mainActivity.onNavigationDrawerItemSelected(0);
	}

	/**
	 * Returns a new instance of this fragment for the given section number.
	 */
	public static Fragment newInstance() {
		Setup2Fragment fragment = new Setup2Fragment();
		Bundle args = new Bundle();
		// args.putInt(ARG_SECTION_NUMBER, sectionNumber);
		fragment.setArguments(args);
		return fragment;
	}
}
