package de.uks.challenger.ui.progress;

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

public class ProgressFragment extends Fragment implements View.OnClickListener {
	private ListView mProgressListView;
	private Button mAddProgressButton;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_progress, container,
				false);

		mProgressListView = (ListView) rootView.findViewById(R.id.progressList);
		mProgressListView.setAdapter(new ProgressAdapter());
		mProgressListView
				.setOnItemClickListener(new ProgressOnItemClickListener());

		mAddProgressButton = (Button) rootView
				.findViewById(R.id.addNewProgressButton);
		mAddProgressButton.setOnClickListener(this);

		return rootView;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

	}

	@Override
	public void onClick(View v) {
		final EditText txtUrl = new EditText(getActivity());
		txtUrl.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);

		// Set the default text to a link of the Queen
		txtUrl.setHint("http://www.librarising.com/astrology/celebs/images2/QR/queenelizabethii.jpg");

		new AlertDialog.Builder(getActivity())
				.setTitle("Moustachify Link")
				.setMessage("Paste in the link of an image to moustachify!")
				.setView(txtUrl)
				.setPositiveButton("Moustachify",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								String text = txtUrl.getText().toString();
								System.out.println(text);
							}
						})
				.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
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
				rowView = inflater.inflate(android.R.layout.simple_list_item_1,
						null);
			}

			// fill data
			TextView text1 = (TextView) rowView
					.findViewById(android.R.id.text1);
			text1.setText(getItem(position).getCreationDate().toString());

			return rowView;
		}
	}

	private final class ProgressOnItemClickListener implements
			OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// Toast.makeText(getActivity(), "SELECTED",
			// Toast.LENGTH_SHORT).show();
		}
	}
}
