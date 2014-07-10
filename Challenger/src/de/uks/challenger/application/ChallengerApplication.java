package de.uks.challenger.application;

import android.app.Application;
import android.content.Intent;
import android.util.Log;
import de.uks.challenger.model.Challenger;
import de.uks.challenger.model.ModelObserver;
import de.uks.challenger.model.User;
import de.uks.challenger.persistence.Source;

/**
 * Application class to do some init stuff, like init the model
 * 
 * @author philipp
 * 
 */
public class ChallengerApplication extends Application {
	private static final String TAG = "Challenger.Application";
	private ModelObserver modelObserver;

	@Override
	public void onCreate() {
		super.onCreate();

		// init model
		initModel();

		// observe model
		modelObserver = new ModelObserver(getApplicationContext());
		modelObserver.observeModel(Challenger.getInstance());

		// start notification service
		Intent service = new Intent(getApplicationContext(), ChallengerService.class);
		getApplicationContext().startService(service);

	}

	private void initModel() {
		Challenger challenger = Challenger.getInstance();

		// read data from db
		Source source = new Source(getApplicationContext());
		source.open();

		User user = source.getUser();

		challenger.setUser(user);

		Log.i(TAG, "Init model finished");

	}
}
