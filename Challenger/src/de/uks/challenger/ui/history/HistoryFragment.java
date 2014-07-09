package de.uks.challenger.ui.history;

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
import de.uks.challenger.model.Unit;
import de.uks.challenger.ui.MainActivity;

public class HistoryFragment extends Fragment {
	private ListView mHistoryListView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_history, container,
				false);

		mHistoryListView = (ListView) rootView.findViewById(R.id.historyList);
		mHistoryListView.setAdapter(new HistoryAdapter());
		mHistoryListView
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
		HistoryFragment fragment = new HistoryFragment();
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
			return Challenger.getInstance().getUser().countOfUnits();
		}

		@Override
		public Unit getItem(int position) {
			return Challenger.getInstance().getUser().getUnit(position);
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
				// configure view holder
				// ViewHolder viewHolder = new ViewHolder();
				// viewHolder.text = (TextView)
				// rowView.findViewById(R.id.TextView01);
				// viewHolder.image = (ImageView) rowView
				// .findViewById(R.id.ImageView01);
				// rowView.setTag(viewHolder);
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

			Intent notificationIntent = new Intent(getActivity(),
					MainActivity.class);
			int YOUR_PI_REQ_CODE = 1337;
			PendingIntent contentIntent = PendingIntent.getActivity(
					getActivity(), YOUR_PI_REQ_CODE , notificationIntent,
					PendingIntent.FLAG_CANCEL_CURRENT);

		}
	}
}
