package de.uks.challenger.ui.progress;

import android.app.Activity;
import android.app.Fragment;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import de.uks.challenger.R;
import de.uks.challenger.model.Challenger;
import de.uks.challenger.model.Progress;
import de.uks.challenger.model.Unit;
import de.uks.challenger.ui.MainActivity;

public class ProgressFragment extends Fragment {
	private ListView mProgressListView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_history, container,
				false);

		mProgressListView = (ListView) rootView.findViewById(R.id.historyList);
		mProgressListView.setAdapter(new HistoryAdapter());
		mProgressListView
				.setOnItemClickListener(new HistoryOnItemClickListener());

		return rootView;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

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

	private class HistoryAdapter extends BaseAdapter {
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

	private final class HistoryOnItemClickListener implements
			OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			Toast.makeText(getActivity(), "SELECTED", Toast.LENGTH_SHORT)
					.show();

		}
	}
}
