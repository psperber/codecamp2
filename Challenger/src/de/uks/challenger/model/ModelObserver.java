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
	public void observeModel(Challenger challenger) {
		observeChallenger(challenger);

		User user = Challenger.getInstance().getUser();
		observeUser(user);
	}

	/**
	 * Starts observation of challenger.
	 * 
	 * @param challenger
	 */
	private void observeChallenger(Challenger challenger) {
		Log.i(TAG, "Start observing of challenger");
		challenger.addPropertyChangeListener(Challenger.PROP_SET_USER, new SetUserListener());
	}

	/**
	 * Starts observation of the user.
	 * 
	 * @param user
	 */
	private void observeUser(User user) {
		if (user == null) {
			Log.e(TAG, "Could not observe user, because user is null");
			return;
		}

		Log.i(TAG, "Start observing of user");
		user.addPropertyChangeListener(User.PROP_ADD_PROGRESS, new AddProgressListener());
		user.addPropertyChangeListener(User.PROP_ADD_UNIT, new AddUnitListener());
		user.addPropertyChangeListener(User.PROP_REMOVE_UNIT, new RemoveUnitListener());
		user.addPropertyChangeListener(User.PROP_REMOVE_UNIT, new RemoveProgressListener());
		user.addPropertyChangeListener(User.PROP_UPDATE_USER, new UpdateUserListener());

	}

	private class SetUserListener implements PropertyChangeListener {

		@Override
		public void propertyChange(PropertyChangeEvent event) {
			// get the setted user
			User user = (User) event.getNewValue();
			User oldUser = (User) event.getOldValue();
			Source source = new Source(context);

			source.open();
			
			if (user == null) {
				source.deleteUser();
				Log.i(TAG, "Set user to null, delete from database");				
				return;
			}

			if (user.equals(oldUser)) {
				Log.i(TAG, "No need to persist user, because its equal to user in db");
				return;
			}

			// write the user to the database
			
			User createdUser = source.createUser(user);
			if (createdUser != null) {
				// we need to set the id of the user in the model
				user.setId(createdUser.getId());
			}else {
				// TODO What to do here?
				Log.w(TAG, "User could not be persisted...");
			}
			source.close();

			// if user was set, we need tinitModel();o start the observation of
			// the user here
			observeUser(user);

		}

	}

	private class AddProgressListener implements PropertyChangeListener {

		@Override
		public void propertyChange(PropertyChangeEvent event) {
			System.out.println("AHHHHHHHHHHHHHHH");
			Progress progress = (Progress) event.getNewValue();

			Source source = new Source(context);

			source.open();
			Progress createdProgress = source.createProgress(progress, Challenger.getInstance().getUser());
			progress.setId(createdProgress.getId());
			source.close();

		}

	}

	private class AddUnitListener implements PropertyChangeListener {

		@Override
		public void propertyChange(PropertyChangeEvent event) {
			Unit unit = (Unit) event.getNewValue();

			Source source = new Source(context);

			source.open();
			Unit createdUnit = source.createUnit(unit, Challenger.getInstance().getUser());
			if (createdUnit != null) {
				unit.setId(createdUnit.getId());
			} else {
				// TODO What to do here?
				Log.w(TAG, "Unit could not be persisted...");
			}

			source.close();

		}
	}

	private class RemoveUnitListener implements PropertyChangeListener {

		@Override
		public void propertyChange(PropertyChangeEvent event) {
			Unit unit = (Unit) event.getNewValue();

			Source source = new Source(context);
			source.open();
			source.deleteUnit(unit);
			source.close();

		}

	}

	private class RemoveProgressListener implements PropertyChangeListener {

		@Override
		public void propertyChange(PropertyChangeEvent event) {
			Progress progress = (Progress) event.getNewValue();

			Source source = new Source(context);
			source.open();
			source.deleteProgress(progress);
			source.close();

		}

	}
	
	private class UpdateUserListener implements PropertyChangeListener {

		@Override
		public void propertyChange(PropertyChangeEvent event) {
			User user = Challenger.getInstance().getUser();
			
			if(user.getId() == 0){
				//no need to update here. user didn't exist and will be created later
				Log.w(TAG, "No need to update here. user didn't exist and will be created later");
				return;
			}
			
			Source source = new Source(context);
			source.open();
			source.updateUser(user);
			source.close();
			
		}
		
	}

}
