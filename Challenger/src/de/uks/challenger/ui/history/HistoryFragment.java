package de.uks.challenger.ui.history;

import java.text.SimpleDateFormat;

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
import de.uks.challenger.model.Unit.UNIT_TYPE;
import de.uks.challenger.model.Workset;
import de.uks.challenger.ui.MainActivity;

public class HistoryFragment extends Fragment {
	private ListView mHistoryListView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_history, container, false);

		mHistoryListView = (ListView) rootView.findViewById(R.id.historyList);
		mHistoryListView.setAdapter(new HistoryAdapter());
		mHistoryListView.setOnItemClickListener(new HistoryOnItemClickListener());

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
				rowView = inflater.inflate(R.layout.history_row, null);

				// configure view holder
				// ViewHolder viewHolder = new ViewHolder();
				// viewHolder.text = (TextView)
				// rowView.findViewById(R.id.TextView01);
				// viewHolder.image = (ImageView) rowView
				// .findViewById(R.id.ImageView01);
				// rowView.setTag(viewHolder);
			}

			// fill data

			Unit unit = getItem(position);

			TextView tv = (TextView) rowView.findViewById(R.id.tvUnitType);
			tv.setText(formatUnitType(unit.getUnitType()));

			tv = (TextView) rowView.findViewById(R.id.tvWeight);
			
			SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyy HH:mm");
			tv.setText(format.format(unit.getCreationDate()));
			
			tv = (TextView) rowView.findViewById(R.id.tvWorksets);
			String text = "";
			for(int i = 0; i < unit.countOfWorksets(); ++i){
				Workset w = unit.getWorkset(i);
				text = text + "Set " + (i + 1) + ": " + w.getCount() + " (" + w.getTodo() + ") ";
			}
			
			tv.setText(text);

			return rowView;
		}
	}

	private String formatUnitType(UNIT_TYPE type) {
		if (UNIT_TYPE.PUSH_UPS.equals(type)) {
			return "Push Ups";
		} else if (UNIT_TYPE.JUMPING_JACK.equals(type)) {
			return "Jumping Jack";
		} else {
			return "Sit Ups";
		}
	}

	private final class HistoryOnItemClickListener implements OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			// Toast.makeText(getActivity(), "SELECTED",
			// Toast.LENGTH_SHORT).show();

		}
	}
}
