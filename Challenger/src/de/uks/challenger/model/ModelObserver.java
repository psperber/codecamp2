package de.uks.challenger.model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import android.content.Context;
import android.util.Log;
import de.uks.challenger.persistence.Source;

/**
 * Class which observes the model and write changes to the database
 * 
 * @author philipp
 * 
 */
public class ModelObserver {
	private static final String TAG = "Challenger.ModelObserver";
	
	private Context context;

	public ModelObserver(Context context) {
		this.context = context;
	}

	/**
	 * Starts observing of the model and writes the changes to the database.
	 * 
	 * @param challenger
	 */
	public void observeModel(Challenger challenger){
		observeChallenger(challenger);
		
		User user = Challenger.getInstance().getUser();
		observeUser(user);
	}
	
	private void observeChallenger(Challenger challenger){
		Log.i(TAG, "Start observing of challenger");
		challenger.addPropertyChangeListener(Challenger.PROP_SET_USER, new SetUserListener());
	}
	
	private void observeUser(User user){
		if(user == null){
			Log.e(TAG, "Could not observe user, because user is null");
			return;
		}
		
		Log.i(TAG, "Start observing of user");
		user.addPropertyChangeListener(User.PROP_ADD_PROGRESS, new AddProgressListener());
		user.addPropertyChangeListener(User.PROP_ADD_UNIT, new AddUnitListener());
		
	}

	private class SetUserListener implements PropertyChangeListener {

		@Override
		public void propertyChange(PropertyChangeEvent event) {
			// get the setted user
			User user = (User) event.getNewValue();
			User oldUser = (User) event.getOldValue();
			if(user == null){
				Log.i(TAG, "No need to persist user, because user is null");
				return;
			}
			
			if(user.equals(oldUser)){
				Log.i(TAG, "No need to persist user, because its equal to user in db");
				return;
			}

			// write the user to the database
			Source source = new Source(context);

			source.open();
			User createdUser = source.createUser(user);
			
			//we need to set the id of the user in the model
			user.setId(createdUser.getId());
			source.close();

			// if user was set, we need tinitModel();o start the observation of the user here
			observeUser(user);
			
		}

	}

	private class AddProgressListener implements PropertyChangeListener {

		@Override
		public void propertyChange(PropertyChangeEvent event) {
			Progress progress = (Progress) event.getNewValue();

			Source source = new Source(context);

			source.open();
			Progress createdProgress = source.createProgress(progress, Challenger.getInstance().getUser());
			progress.setId(createdProgress.getId());
			source.close();

		}

	}
	
	private class AddUnitListener implements PropertyChangeListener{

		@Override
		public void propertyChange(PropertyChangeEvent event) {
			Unit unit = (Unit) event.getNewValue();
			
			Source source = new Source(context);
			
			source.open();
			Unit createdUnit = source.createUnit(unit, Challenger.getInstance().getUser());
			unit.setId(createdUnit.getId());
			source.close();
			
		}
		
	}

}
