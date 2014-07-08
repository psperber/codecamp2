package de.uks.challenger.ui;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.uks.challenger.R;
import de.uks.challenger.model.Challenger;
import de.uks.challenger.model.Unit;
import de.uks.challenger.model.User;
import de.uks.challenger.model.Workset;
import de.uks.challenger.ui.graph.GraphFragment;
import de.uks.challenger.ui.history.HistoryFragment;
import de.uks.challenger.ui.settings.SettingsFragment;
import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.widget.ArrayAdapter;
import android.widget.TextView;

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
		
		// DUMMYMODEL
		Challenger challenger = Challenger.getInstance();
		User user = new User();
		
		Unit unit = new Unit();
		unit.setCreationDate(new Date(System.currentTimeMillis() - 1000 * 60 * 60));
		Workset workset = new Workset();
		workset.setCount(25);
		unit.addWorkset(workset);
		workset = new Workset();
		workset.setCount(50);
		unit.addWorkset(workset);
		user.addUnit(unit);
		
		unit = new Unit();;
		unit.setCreationDate(new Date(System.currentTimeMillis() - 1000 * 60 * 60 * 20));
		user.addUnit(unit);
		
		unit = new Unit();;
		unit.setCreationDate(new Date(System.currentTimeMillis() - 1000 * 60 * 60 * 150));
		workset = new Workset();
		workset.setCount(10);
		unit.addWorkset(workset);
		user.addUnit(unit);
		
		challenger.setUser(user);
		
		mNavigationDrawerFragment = (NavigationDrawerFragment) getFragmentManager()
				.findFragmentById(R.id.navigation_drawer);
		mTitle = getTitle();

		// Set up the drawer.
		mNavigationDrawerFragment.setUp(R.id.navigation_drawer,
				(DrawerLayout) findViewById(R.id.drawer_layout));
	}

	@Override
	public void onNavigationDrawerItemSelected(int position) {
		Fragment fragment = null;

		switch (position) {
		case 0:
			fragment = HistoryFragment.newInstance();
			break;
		case 1:
			fragment = GraphFragment.newInstance();
			break;
		case 2:
			fragment = SettingsFragment.newInstance();
			break;
		}
		
		if (fragment != null) {
			String[] titles = getResources().getStringArray(R.array.title_section);
			mTitle = titles[position];

			// update the main content by replacing fragments
			FragmentManager fragmentManager = getFragmentManager();
			fragmentManager.beginTransaction().replace(R.id.container, fragment)
					.commit();
		} else {
			mTitle = "unselected";
		}
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

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
