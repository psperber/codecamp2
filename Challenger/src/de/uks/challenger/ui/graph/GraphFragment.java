package de.uks.challenger.ui.graph;

import de.uks.challenger.R;
import de.uks.challenger.R.layout;
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
import android.widget.Toast;

public class GraphFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_graph, container,
				false);

//		rootView.findViewById(R.id.button1).setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				SharedPreferences sp = PreferenceManager
//						.getDefaultSharedPreferences(getActivity());
//				boolean isinit = sp.getBoolean("initdone", false);
//				if (!isinit) {
//					Editor edit = sp.edit();
//					edit.putBoolean("initdone", true);
//					edit.commit();
//					Toast.makeText(getActivity(), "SET TO true", Toast.LENGTH_SHORT).show();
//				} else {
//					Editor edit = sp.edit();
//					edit.putBoolean("initdone", false);
//					edit.commit();
//					Toast.makeText(getActivity(), "SET TO false", Toast.LENGTH_SHORT).show();
//				}
//			}
//		});
		
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
		GraphFragment fragment = new GraphFragment();
		Bundle args = new Bundle();
		// args.putInt(ARG_SECTION_NUMBER, sectionNumber);
		fragment.setArguments(args);
		return fragment;
	}
}
