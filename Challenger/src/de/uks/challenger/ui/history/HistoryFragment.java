package de.uks.challenger.ui.history;

import de.uks.challenger.R;
import de.uks.challenger.R.id;
import de.uks.challenger.R.layout;
import de.uks.challenger.model.Challenger;
import de.uks.challenger.model.Unit;
import android.app.Activity;
import android.app.Fragment;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class HistoryFragment extends Fragment {
	private ListView mHistoryListView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_history, container,
				false);

		mHistoryListView = (ListView) rootView.findViewById(R.id.historyList);
		mHistoryListView.setAdapter(new HistoryAdapter());
		mHistoryListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Toast.makeText(getActivity(), "SELECTED", Toast.LENGTH_SHORT).show();;
			}
		});

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
			return Challenger.getInstance().getUser().getUnits().size();
		}

		@Override
		public Unit getItem(int position) {
			return Challenger.getInstance().getUser().getUnits().get(position);
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
		      rowView = inflater.inflate(android.R.layout.simple_list_item_1, null);
		      // configure view holder
//		      ViewHolder viewHolder = new ViewHolder();
//		      viewHolder.text = (TextView) rowView.findViewById(R.id.TextView01);
//		      viewHolder.image = (ImageView) rowView
//		          .findViewById(R.id.ImageView01);
//		      rowView.setTag(viewHolder);
		    }

		    // fill data
		    TextView text1 = (TextView) rowView.findViewById(android.R.id.text1);
		    text1.setText(getItem(position).getCreationDate().toString());

		    return rowView;
		}
	}
}
