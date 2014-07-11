package de.uks.challenger.ui.history;



import android.app.Activity;
import android.app.Fragment;
import android.os.AsyncTask;
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
import de.uks.challenger.model.User;
import de.uks.challenger.social.Tweet;

/**
 * Displays the history of the workout units
 * 
 * @author Comtec
 *
 */
public class HistoryFragment extends Fragment {
	private ListView mHistoryListView;
	Tweet tweet = Tweet.getInstance();
	

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
	
	/**
	 * Controls the List
	 * 
	 * @author Comtec
	 *
	 */
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
				rowView = inflater.inflate(android.R.layout.simple_list_item_1, null);
				// configure view holder
				// ViewHolder viewHolder = new ViewHolder();
				// viewHolder.text = (TextView)
				// rowView.findViewById(R.id.TextView01);
				// viewHolder.image = (ImageView) rowView
				// .findViewById(R.id.ImageView01);
				// rowView.setTag(viewHolder);
			}

			// fill data
			TextView text1 = (TextView) rowView.findViewById(android.R.id.text1);
			text1.setText(getItem(position).getCreationDate().toString());

			return rowView;
		}
	}
	
	/**
	 * On Clicklistener
	 * 
	 * @author Comtec
	 *
	 */
	private final class HistoryOnItemClickListener implements OnItemClickListener {
		@Override

		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
//			Toast.makeText(getActivity(), "SELECTED", Toast.LENGTH_SHORT).show();
			String m = "dies ist ein test #wm14";
			TaskSend tasksend = new TaskSend(m);
			tasksend.execute();
	
		}
	}
	
	// Asynchroner Task zum twittern
		private class TaskSend extends AsyncTask<Void, Void, String> {
			private String text;

			public TaskSend(String text) {
				this.text = text;
			}

			@Override
			protected String doInBackground(Void... params) {
				
				Challenger challenger = Challenger.getInstance();
				User user = challenger.getUser();
				String savedAccessToken = user.getSavedAccessToken();
				String savedAccessTokenSecret = user.getSavedAccessTokenSecret();
			
				if (savedAccessToken.length() == 0 || savedAccessTokenSecret.length() == 0) {
					printTwitterError();

				} else {
					try {
						tweet.tweet(text, savedAccessToken, savedAccessTokenSecret);
					} catch (Exception e) {
						printTwitterError();
					}
				}
				return null;
			}
		}

		// Prints Twitter Error on UI
		private void printTwitterError() {
			getActivity().runOnUiThread(new Runnable() {
				@Override
				public void run() {
					Toast.makeText(getActivity().getApplicationContext(), "Fehlerhafter Login, bitte in den Einstellungen korrigieren", Toast.LENGTH_SHORT).show();
				}
			});
		}
}
