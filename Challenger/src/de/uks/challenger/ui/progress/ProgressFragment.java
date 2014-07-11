package de.uks.challenger.ui.progress;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import de.uks.challenger.R;
import de.uks.challenger.model.Challenger;
import de.uks.challenger.model.Progress;
import de.uks.challenger.model.User;

public class ProgressFragment extends Fragment implements View.OnClickListener {
	private ListView mProgressListView;
	private Button mAddProgressButton;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_progress, container, false);

		mProgressListView = (ListView) rootView.findViewById(R.id.progressList);
		mProgressListView.setAdapter(new ProgressAdapter());
		mProgressListView.setOnItemClickListener(new ProgressOnItemClickListener());

		mAddProgressButton = (Button) rootView.findViewById(R.id.addNewProgressButton);
		mAddProgressButton.setOnClickListener(this);

		return rootView;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

	}

	@Override
	public void onClick(View v) {
		final EditText weightEditText = new EditText(getActivity());
		weightEditText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

		new AlertDialog.Builder(getActivity()).setTitle(R.string.progress_dialog_title).setMessage(R.string.progress_dialog_message).setView(weightEditText).setPositiveButton(R.string.progress_dialog_okay, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				String weightString = weightEditText.getText().toString();
				if (!"".equals(weightString)) {
					double weight = Double.valueOf(weightString);

					User user = Challenger.getInstance().getUser();
					Progress progress = new Progress();
					progress.setCreationDate(new Date());
					progress.setAge(user.getAge());
					progress.setWeight(weight);
					user.addProgress(progress);

					mProgressListView.invalidateViews();
				}
			}
		}).setNegativeButton(R.string.progress_dialog_cancel, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
			}
		}).show();
	}

	/**
	 * Returns a new instance of this fragment for the given section number.
	 */
	public static Fragment newInstance() {
		ProgressFragment fragment = new ProgressFragment();
		Bundle args = new Bundle();
		// args.putInt(ARG_SECTION_NUMBER, sectionNumber);
		fragment.setArguments(args);
		return fragment;
	}

	private class ProgressAdapter extends BaseAdapter {
		@Override
		public int getCount() {
			if (Challenger.getInstance().getUser() == null) {
				return 0;
			}
			return Challenger.getInstance().getUser().countOfProgress();
		}

		@Override
		public Progress getItem(int position) {
			return Challenger.getInstance().getUser().getProgress(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View rowView = convertView;
			// reuse views
			if (rowView == null) {
				LayoutInflater inflater = getActivity().getLayoutInflater();
				rowView = inflater.inflate(R.layout.progress_row, null);
			}

			// fill data
			TextView tv = (TextView) rowView.findViewById(R.id.tvDate);

			Progress progress = getItem(position);

			SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyy HH:mm");
			tv.setText(format.format(progress.getCreationDate()));

			tv = (TextView) rowView.findViewById(R.id.tvWeight);
			tv.setText("Age: " + progress.getAge() + " Weight: " + progress.getWeight());

			return rowView;
		}
	}

	private final class ProgressOnItemClickListener implements OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			// Toast.makeText(getActivity(), "SELECTED",
			// Toast.LENGTH_SHORT).show();
		}
	}
}
