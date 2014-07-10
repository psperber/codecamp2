package de.uks.challenger.ui.graph;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.jjoe64.graphview.CustomLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GraphViewSeries;
import com.jjoe64.graphview.LineGraphView;

import de.uks.challenger.R;
import de.uks.challenger.model.Challenger;
import de.uks.challenger.model.Progress;
import de.uks.challenger.model.User;

public class GraphFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		View rootView = inflater.inflate(R.layout.fragment_graph, container, false);
		
		User user = Challenger.getInstance().getUser();
		if(user.countOfProgress() != 0) {
			
			Iterator<Progress> iter = user.getProgressIterator();
			GraphData[] data = new GraphData[user.countOfProgress()];
			int i = 0;
			for (Iterator<Progress> it = iter; it.hasNext();) {
				
				Progress prog = it.next();
				
				double weight = prog.getWeight();
				Date creationDate = prog.getCreationDate();
				
				data[i] = new GraphData(creationDate.getTime(), weight);
				
				i++;
			}
			
				 
	        
	        
	    	GraphView graphView = new LineGraphView( getActivity(), "Weight processing...");
	    	
	    	graphView.setCustomLabelFormatter(new CustomLabelFormatter() {
	    		DateFormat dateFormat = new SimpleDateFormat("dd.MM.");
	    		
	            @Override
	            public String formatLabel(double value, boolean isValueX) {
	            	
	                if (isValueX) {
	                    Date d = new Date((long) (value));
	                    
						return (dateFormat.format(d));
	                }
	                return "" + (int) value;
	            }
	        });
	    	
	    	
	    	GraphViewSeries exampleSeries = new GraphViewSeries(data);
	    	graphView.addSeries(exampleSeries);
	    	
	    	//graphView.setViewPort(2, 40);
	    	graphView.setScrollable(true);
	    	
	    	// optional - activate scaling / zooming
	    	graphView.setScalable(true);

	    	 
	    	LinearLayout layout = (LinearLayout) rootView.findViewById(R.id.graphLayout);
	    	layout.addView(graphView);

		}
		
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
