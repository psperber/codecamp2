package de.uks.challenger.ui;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import de.uks.challenger.R;
import de.uks.challenger.model.Challenger;
import de.uks.challenger.model.Progress;
import de.uks.challenger.model.User;
import de.uks.challenger.ui.attack.AttackFragment;
import de.uks.challenger.ui.graph.GraphFragment;
import de.uks.challenger.ui.history.HistoryFragment;
import de.uks.challenger.ui.progress.ProgressFragment;
import de.uks.challenger.ui.settings.SettingsFragment;
import de.uks.challenger.ui.setup.Setup1Fragment;

public class MainActivity extends Activity implements
		NavigationDrawerFragment.NavigationDrawerCallbacks {

	/**
	 * Fragment managing the behaviors, interactions and presentation of the
	 * navigation drawer.
	 */
	private NavigationDrawerFragment mNavigationDrawerFragment;

	/**
	 * Used to store the last screen title. For use in
	 * {@link #restoreActionBar()}.
	 */
	private CharSequence mTitle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mNavigationDrawerFragment = (NavigationDrawerFragment) getFragmentManager()
				.findFragmentById(R.id.navigation_drawer);
		String[] titles = getResources().getStringArray(R.array.title_section);
		mTitle = titles[0];

		// Set up the drawer.
		
		User user = Challenger.getInstance().getUser();
		if (user == null) {
			mTitle = getString(R.string.title_setup);
			restoreActionBar();
			Fragment fragment = Setup1Fragment.newInstance();
			FragmentManager fragmentManager = getFragmentManager();
			fragmentManager.beginTransaction()
					.replace(R.id.container, fragment).commit();
		}
		
		mNavigationDrawerFragment.setUp(R.id.navigation_drawer,
				(DrawerLayout) findViewById(R.id.drawer_layout));
		
		

	}

	@Override
	public void onNavigationDrawerItemSelected(int position) {
		Fragment fragment = null;
		switch (position) {
		case 0: {
			fragment = SettingsFragment.newInstance();
			break;
		}
		case 1: {
			fragment = HistoryFragment.newInstance();
			break;
		}
		case 2: {
			fragment = ProgressFragment.newInstance();
			break;
		}
		case 3: {
			fragment = GraphFragment.newInstance();
			break;
			
		}
		case 4: {
			fragment = AttackFragment.newInstance();
			break;
		}
		}

		String[] titles = getResources().getStringArray(R.array.title_section);
		mTitle = titles[position];

		// update the main content by replacing fragments
		FragmentManager fragmentManager = getFragmentManager();
		fragmentManager.beginTransaction().replace(R.id.container, fragment)
				.commit();
	}

	public void restoreActionBar() {
		ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setTitle(mTitle);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (!mNavigationDrawerFragment.isDrawerOpen()) {
			// Only show items in the action bar relevant to this screen
			// if the drawer is not showing. Otherwise, let the drawer
			// decide what to show in the action bar.
			getMenuInflater().inflate(R.menu.main, menu);
			restoreActionBar();
			return true;
		}
		return super.onCreateOptionsMenu(menu);
	}

	// @Override
	// public boolean onOptionsItemSelected(MenuItem item) {
	// // Handle action bar item clicks here. The action bar will
	// // automatically handle clicks on the Home/Up button, so long
	// // as you specify a parent activity in AndroidManifest.xml.
	// int id = item.getItemId();
	// if (id == R.id.action_settings) {
	// return true;
	// }
	// return super.onOptionsItemSelected(item);
	// }
}
