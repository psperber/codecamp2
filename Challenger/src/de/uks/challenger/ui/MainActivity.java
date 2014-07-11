package de.uks.challenger.ui;

import java.util.Date;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import de.uks.challenger.R;
import de.uks.challenger.application.ChallengerApplication;
import de.uks.challenger.model.Challenger;
import de.uks.challenger.model.User;
import de.uks.challenger.ui.attack.AttackFragment;
import de.uks.challenger.ui.graph.GraphFragment;
import de.uks.challenger.ui.history.HistoryFragment;
import de.uks.challenger.ui.progress.ProgressFragment;
import de.uks.challenger.ui.settings.SettingsFragment;
import de.uks.challenger.ui.setup.SetupFragment;

public class MainActivity extends Activity implements
		NavigationDrawerFragment.NavigationDrawerCallbacks {

	/**
	 * Fragment managing the behaviors, interactions and presentation of the
	 * navigation drawer.
	 */
	private NavigationDrawerFragment mNavigationDrawerFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mNavigationDrawerFragment = (NavigationDrawerFragment) getFragmentManager()
				.findFragmentById(R.id.navigation_drawer);

		// Set up the drawer.
		mNavigationDrawerFragment.setUp(R.id.navigation_drawer,
				(DrawerLayout) findViewById(R.id.drawer_layout));

		// if user is null, setup is needed
		User user = Challenger.getInstance().getUser();
		if (user == null) {
			Fragment fragment = SetupFragment.newInstance();
			FragmentManager fragmentManager = getFragmentManager();
			fragmentManager.beginTransaction()
					.replace(R.id.container, fragment).commit();
		}

		// check if this activity was started by the service
		boolean startAttack = getIntent().getBooleanExtra(
				getString(R.string.extra_name_attack), false);
		if (startAttack) {
			restoreActionBar();
			Fragment fragment = AttackFragment.newInstance();
			FragmentManager fragmentManager = getFragmentManager();
			fragmentManager.beginTransaction()
					.replace(R.id.container, fragment).commit();

			// delete the notification
			NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
			nm.cancel(ChallengerApplication.NOTIFICATION_ID);
		}

		mNavigationDrawerFragment.setUp(R.id.navigation_drawer,
				(DrawerLayout) findViewById(R.id.drawer_layout));
	}

	@Override
	public void onNavigationDrawerItemSelected(int position) {
		Fragment fragment = null;
		switch (position) {
		case 0: {
			fragment = HistoryFragment.newInstance();
			break;
		}
		case 1: {
			fragment = ProgressFragment.newInstance();
			break;
		}
		case 2: {
			fragment = GraphFragment.newInstance();
			break;
		}
		case 3: {
			fragment = SettingsFragment.newInstance();
			break;

		}
		case 4: {
			fragment = AttackFragment.newInstance();
			break;
		}
		}

		// update the main content by replacing fragments
		FragmentManager fragmentManager = getFragmentManager();
		fragmentManager.beginTransaction().replace(R.id.container, fragment)
				.commit();
	}

	public void restoreActionBar() {
		ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setTitle(R.string.app_name);
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

	public void selectItem(int position) {
		mNavigationDrawerFragment.selectItem(position);
	}

	// @Override
	// public boolean onOptionsItemSelected(MenuItem item) {
	// // Handle action bar item clicks here. The action bar will
	// // automatically handle clicks on the Home/Up button, so long
	// // as you specify a parent activity in AndroidManifest.xml.
	// int id = item.getItemId();
	// if (id == R.id.action_add_progress) {
	// System.out.println("ADD PROGRESS");
	// return true;
	// }
	// return super.onOptionsItemSelected(item);
	// }
}
