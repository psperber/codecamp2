package de.uks.challenger.ui.graph;

import java.util.Iterator;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import de.uks.challenger.R;
import de.uks.challenger.model.Challenger;
import de.uks.challenger.model.Progress;
import de.uks.challenger.model.User;

public class GraphFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_graph, container,
				false);

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

	public static class DrawView extends View {
		private static final int BORDER_OFFSET_X = 5;
		private static final int BORDER_OFFSET_Y = 10;

		Paint mCoordPaint;

		public DrawView(Context context) {
			super(context);
			init();
		}

		public DrawView(Context context, AttributeSet attrs) {
			super(context, attrs);
			init();
		}

		public DrawView(Context context, AttributeSet attrs, int defStyleAttr) {
			super(context, attrs, defStyleAttr);
			init();
		}

		private void init() {
			mCoordPaint = new Paint();
			mCoordPaint.setStyle(Paint.Style.STROKE);
			// mCoordPaint.setColor(Color.parseColor("#CD5C5C"));
			mCoordPaint.setColor(Color.BLACK);
		}

		@Override
		protected void onDraw(Canvas canvas) {
			// TODO Auto-generated method stub
			super.onDraw(canvas);

			int width = getWidth();
			int height = getHeight();

			float startX, startY, stopX, stopY;

			// draw coordinate axes
			startX = BORDER_OFFSET_X;
			startY = BORDER_OFFSET_Y;
			stopX = BORDER_OFFSET_X;
			stopY = height - BORDER_OFFSET_Y;
			canvas.drawLine(startX, startY, stopX, stopY, mCoordPaint);
			canvas.drawLine(startX, startY, startX - 5, startY + 5, mCoordPaint);
			canvas.drawLine(startX, startY, startX + 5, startY + 5, mCoordPaint);

			startX = BORDER_OFFSET_X;
			startY = height - BORDER_OFFSET_Y;
			stopX = width - BORDER_OFFSET_X;
			stopY = height - BORDER_OFFSET_Y;
			canvas.drawLine(startX, startY, stopX, stopY, mCoordPaint);
			canvas.drawLine(stopX, stopY, stopX - 5, stopY - 5, mCoordPaint);
			canvas.drawLine(stopX, stopY, stopX - 5, stopY + 5, mCoordPaint);

			// draw progress
			if (!isInEditMode()) {
				User user = Challenger.getInstance().getUser();
				int countOfProgress = user.countOfProgress();
				if (countOfProgress > 1) {
					double minWeight = Float.MAX_VALUE;
					double maxWeidht = Float.MIN_NORMAL;
					
					for (Iterator<Progress> iterator = user.getProgressIterator();
							iterator.hasNext();) {
						Progress progress = (Progress) iterator.next();

						minWeight = Math.min(minWeight, progress.getWeight());
						maxWeidht = Math.max(maxWeidht, progress.getWeight());
					}
					
					Progress earliestProgress = user.getProgress(0);
					Progress latestProgress = user.getProgress(countOfProgress - 1);
					
					float x = 100;
					Path path = new Path();
					for (Iterator<Progress> iterator = user.getProgressIterator();
							iterator.hasNext();) {
						Progress progress = (Progress) iterator.next();
						
						double normWeight = (progress.getWeight() - minWeight) / (maxWeidht - minWeight);
						float y = height - BORDER_OFFSET_Y;
						y -= (height - 2 * BORDER_OFFSET_Y) * normWeight;
						
						path.lineTo(x, y);
						
						x = x + 100;
					}
					canvas.drawPath(path, mCoordPaint);
				}
			}
		}
	}
}
