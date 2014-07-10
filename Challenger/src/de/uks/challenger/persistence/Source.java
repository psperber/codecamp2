package de.uks.challenger.persistence;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import de.uks.challenger.model.Progress;
import de.uks.challenger.model.Unit;
import de.uks.challenger.model.Unit.UNIT_TYPE;
import de.uks.challenger.model.User;
import de.uks.challenger.model.User.GENDER;
import de.uks.challenger.model.Workset;

public class Source {
	private static final String TAG = "Challenger.Source";
	private static final String[] UNIT_COLLUMNS = { DatabaseHelper.UNITS_COLUMN_ID, DatabaseHelper.UNITS_COLUMN_CREATION_DATE, DatabaseHelper.UNITS_COLUMN_UNIT_TYPE };
	private static final String[] USER_COLLUMNS = { DatabaseHelper.USER_COLUMN_ID, DatabaseHelper.USER_COLUMN_GENDER, DatabaseHelper.USER_COLUMN_HEIGHT, DatabaseHelper.USER_COLUMN_RESTING_TIME, DatabaseHelper.USER_COLUMN_BIRTHDAY };
	private static final String[] WORKSET_COLLUMNS = { DatabaseHelper.WORKSETS_COLUMN_ID, DatabaseHelper.WORKSETS_COLUMN_COUNT, DatabaseHelper.WORKSETS_COLUMN_TODO };
	private static final String[] PROGRESS_COLLUMNS = { DatabaseHelper.PROGRESS_COLUMN_ID, DatabaseHelper.PROGRESS_COLUMN_CREATION_DATE, DatabaseHelper.PROGRESS_COLUMN_AGE, DatabaseHelper.PROGRESS_COLUMN_WEIGHT,
			DatabaseHelper.PROGRESS_COLUMN_ID_USER };

	private static final SimpleDateFormat datetimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	private SQLiteDatabase database;
	private DatabaseHelper dbHelper;
	private Context context;

	public Source(Context context) {
		dbHelper = new DatabaseHelper(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	protected SQLiteDatabase getDatabase() {
		return database;
	}

	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	/**
	 * Persists the unit. Run iteturns the persisted unit.
	 * 
	 * Returns null if something bad happened
	 * 
	 * @param unit
	 * @return
	 */
	public Unit createUnit(Unit unit, User user) {
		ContentValues values = new ContentValues();

		// format the date
		if (unit.getCreationDate() != null) {
			String formattedDate = datetimeFormat.format(unit.getCreationDate());
			values.put(DatabaseHelper.UNITS_COLUMN_CREATION_DATE, formattedDate);
		} else {
			Log.e(TAG, "Can't create unit. No creation date given");
			return null;
		}

		if (unit.getUnitType() != null) {
			values.put(DatabaseHelper.UNITS_COLUMN_UNIT_TYPE, unit.getUnitType().ordinal());
		} else {
			Log.e(TAG, "Can't create unit. No unit type");
			return null;
		}

		if (user.getId() != 0) {
			values.put(DatabaseHelper.UNITS_COLUMN_ID_USER, user.getId());
		} else {
			Log.e(TAG, "Can't create unit. No id_user given");
			return null;
		}

		long insertId = getDatabase().insert(DatabaseHelper.TABLE_UNITS, null, values);

		if (insertId >= 0) {
			// successfull insertion

			Cursor cursor = getDatabase().query(DatabaseHelper.TABLE_UNITS, UNIT_COLLUMNS, DatabaseHelper.UNITS_COLUMN_ID + " = " + insertId, null, null, null, null);

			Unit fetchedUnit = cursorToUnit(cursor);
			cursor.close();

			// save the worksets
			for (Iterator<Workset> it = unit.getWorksetIterator(); it.hasNext();) {
				Workset workset = it.next();

				Workset createWorkSet = createWorkSet(workset, fetchedUnit);
				if (createWorkSet != null) {
					// everything fine while creating workset
					fetchedUnit.addWorkset(workset);
				} else {
					// error while creating workset, delete inserted elements
					deleteUnit(fetchedUnit);
					Log.e(TAG, "Error while creating unit. Could not create workset. Delete");
					return null;
				}
			}

			Log.i(TAG, "Successfully created unit");
			return fetchedUnit;
		} else {
			Log.e(TAG, "Could not insert unit to database");
			return null;
		}
	}

	/**
	 * Returns all units from user
	 * 
	 * @return
	 */
	public List<Unit> getUnitsFromUser(User user) {
		List<Unit> units = new ArrayList<Unit>();

		Cursor cursor = getDatabase().query(DatabaseHelper.TABLE_UNITS, UNIT_COLLUMNS, DatabaseHelper.UNITS_COLUMN_ID_USER + " = " + user.getId(), null, null, null, null);

		while (!cursor.isAfterLast()) {
			Unit unit = cursorToUnit(cursor);
			if (unit != null) {
				// get worksets to the unit
				List<Workset> worksets = getWorksets(unit);
				for (Workset workset : worksets) {
					unit.addWorkset(workset);
				}

				units.add(unit);
			}

			cursor.moveToNext();
		}
		// make sure to close the cursor
		cursor.close();
		return units;
	}
	
	public List<Progress> getProgressDataFromUser(User user){
		List<Progress> progressData = new ArrayList<Progress>();
		Cursor cursor = getDatabase().query(DatabaseHelper.TABLE_PROGRESS, PROGRESS_COLLUMNS, DatabaseHelper.PROGRESS_COLUMN_ID_USER + " = " + user.getId(), null, null, null, null);
		
		while (!cursor.isAfterLast()) {
			Progress progress = cursorToProgress(cursor);
			if (progress != null) {
				progressData.add(progress);
			}

			cursor.moveToNext();
		}
		// make sure to close the cursor
		cursor.close();
		
		return progressData;
		
	}

	/**
	 * Returns all stored units.
	 * 
	 * @return
	 */
	public List<Unit> getAllUnits() {
		List<Unit> units = new ArrayList<Unit>();

		Cursor cursor = getDatabase().query(DatabaseHelper.TABLE_UNITS, UNIT_COLLUMNS, null, null, null, null, null);

		while (!cursor.isAfterLast()) {
			Unit unit = cursorToUnit(cursor);
			if (unit != null) {
				// get worksets to the unit
				List<Workset> worksets = getWorksets(unit);
				for (Workset workset : worksets) {
					unit.addWorkset(workset);
				}

				units.add(unit);
			}

			cursor.moveToNext();
		}
		// make sure to close the cursor
		cursor.close();
		return units;
	}

	/**
	 * Deletes all units from database.
	 */
	public void deleteAllUnits() {
		getDatabase().delete(DatabaseHelper.TABLE_UNITS, null, null);

		deleteAllWorksets();
	}

	/**
	 * Deletes the whole progress data
	 */
	public void deleteAllProgressData() {
		getDatabase().delete(DatabaseHelper.TABLE_PROGRESS, null, null);
	}

	/**
	 * Deletes the given unit
	 * 
	 * @param unit
	 */
	public void deleteUnit(Unit unit) {
		// delete the unit
		getDatabase().delete(DatabaseHelper.TABLE_UNITS, DatabaseHelper.UNITS_COLUMN_ID + " = " + unit.getId(), null);

		// delete the worksets from the unit
		deleteWorksetsFromUnit(unit);

	}
	
	/**
	 * Deletes the given progress data
	 * @param progress
	 */
	public void deleteProgress(Progress progress){
		getDatabase().delete(DatabaseHelper.TABLE_PROGRESS, DatabaseHelper.PROGRESS_COLUMN_ID + " = " + progress.getId(), null);
	}

	/**
	 * Deletes the worksets from a unit. }
	 * 
	 * @param unit
	 */
	private void deleteWorksetsFromUnit(Unit unit) {
		getDatabase().delete(DatabaseHelper.TABLE_WORKSETS, DatabaseHelper.WORKSETS_COLUMN_ID_UNIT + " = " + unit.getId(), null);
	}

	/**
	 * Converts cursor to unit instance.
	 * 
	 * @param cursor
	 * @return
	 */
	private Unit cursorToUnit(Cursor cursor) {
		if (cursor.moveToFirst()) {
			Unit unit = new Unit();
			unit.setId(cursor.getLong(0));

			try {
				unit.setCreationDate(datetimeFormat.parse(cursor.getString(1)));
			} catch (ParseException e) {
				e.printStackTrace();
			}

			int unitType = cursor.getInt(2);
			if (unitType == 0) {
				unit.setUnitType(UNIT_TYPE.PUSH_UPS);
			} else if (unitType == 1) {
				unit.setUnitType(UNIT_TYPE.SIT_UPS);
			} else {
				unit.setUnitType(UNIT_TYPE.JUMPING_JACK);
			}

			return unit;
		} else {
			return null;
		}

	}

	/**
	 * Persists user to database and returns persisted user.
	 * 
	 * Returns null if something bad happened
	 * 
	 * @param user
	 * @return
	 */
	public User createUser(User user) {
		if (user == null) {
			Log.e(TAG, "Can't create user. User is null");
			return null;
		}

		ContentValues values = new ContentValues();

		// format the date
		if (user.getGender() != null) {
			values.put(DatabaseHelper.USER_COLUMN_GENDER, user.getGender().ordinal());
		} else {
			Log.e(TAG, "Can't create user. No gender given");
			return null;
		}

		if (user.getHeight() != 0) {
			values.put(DatabaseHelper.USER_COLUMN_HEIGHT, user.getHeight());
		} else {
			Log.e(TAG, "Can't create user. No height given");
			return null;
		}
		
		if(user.getBirthday() != null){
			values.put(DatabaseHelper.USER_COLUMN_BIRTHDAY, datetimeFormat.format(user.getBirthday()));
		}else{
			Log.e(TAG, "Can't create user. No birthday given");
			return null;
		}
		

		long insertId = getDatabase().insert(DatabaseHelper.TABLE_USER, null, values);
		if (insertId >= 0) {
			// successfull insertion
			Cursor cursor = getDatabase().query(DatabaseHelper.TABLE_USER, USER_COLLUMNS, DatabaseHelper.USER_COLUMN_ID + " = " + insertId, null, null, null, null);

			User fetchedUser = cursorToUser(cursor);
			cursor.close();

			// create units from user
			for (Iterator<Unit> it = user.getUnitIterator(); it.hasNext();) {
				Unit unit = it.next();

				Unit createdUnit = createUnit(unit, fetchedUser);
				if (createdUnit == null) {
					// something bad happened, delete
					Log.e(TAG, "Error while creating user. Could not create unit. Delete");
					deleteUser();
					return null;
				} else {
					fetchedUser.addUnit(createdUnit);
				}
			}

			// create progress data
			for (Iterator<Progress> it = user.getProgressIterator(); it.hasNext();) {
				Progress progress = it.next();

				Progress createdProgress = createProgress(progress, fetchedUser);
				if (createdProgress == null) {
					// something bad happened, delete
					Log.e(TAG, "Error while creating user. Could not create progress. Delete");
					deleteUser();
					return null;
				} else {
					fetchedUser.addProgress(createdProgress);
				}
			}

			Log.i(TAG, "Successfully created user.");
			return fetchedUser;
		} else {
			Log.e(TAG, "Coul'd not insert user to database.");
			return null;
		}
	}

	/**
	 * Writes given progress and returns the persisted progress.
	 * 
	 * Returns null if something bad happened
	 * 
	 * @param progress
	 * @param user
	 * @return
	 */
	public Progress createProgress(Progress progress, User user) {
		ContentValues values = new ContentValues();

		// validate
		if (progress.getCreationDate() != null) {
			values.put(DatabaseHelper.PROGRESS_COLUMN_CREATION_DATE, datetimeFormat.format(progress.getCreationDate()));
		} else {
			Log.e(TAG, "Can't create progress. No creation date given");
			return null;
		}

		if (progress.getAge() != 0) {
			values.put(DatabaseHelper.PROGRESS_COLUMN_AGE, progress.getAge());
		} else {
			Log.e(TAG, "Can't create progress. No age given");
			return null;
		}

		if (!new Double(0.).equals(progress.getWeight())) {
			values.put(DatabaseHelper.PROGRESS_COLUMN_WEIGHT, progress.getWeight());
		} else {
			Log.e(TAG, "Can't create progress. No weight given");
			return null;
		}

		if (user.getId() != 0) {
			values.put(DatabaseHelper.PROGRESS_COLUMN_ID_USER, user.getId());
		} else {
			Log.e(TAG, "Can't create progress. No user id given");
			return null;
		}

		// everything fine here, persist
		long insertId = getDatabase().insert(DatabaseHelper.TABLE_PROGRESS, null, values);
		if (insertId >= 0) {
			// successfull insertion
			Cursor cursor = getDatabase().query(DatabaseHelper.TABLE_PROGRESS, PROGRESS_COLLUMNS, DatabaseHelper.PROGRESS_COLUMN_ID + " = " + insertId, null, null, null, null);

			Progress fetchedProgress = cursorToProgress(cursor);
			cursor.close();

			return fetchedProgress;
		} else {
			Log.e(TAG, "Coul'd not insert progress to database.");
			return null;
		}
	}

	/**
	 * Converts cursor to user instance.
	 * 
	 * @param cursor
	 * @return
	 */
	private User cursorToUser(Cursor cursor) {

		if (cursor.moveToFirst()) {
			User user = new User();
			user.setId(cursor.getLong(0));

			if (0 == cursor.getInt(1)) {
				user.setGender(GENDER.MALE);
			} else {
				user.setGender(GENDER.FEMALE);
			}

			user.setHeight(cursor.getInt(2));
			user.setRestingTime(cursor.getInt(3));
			
			try {
				user.setBirthday(datetimeFormat.parse(cursor.getString(4)));
			} catch (ParseException e) {
				//should never happen
				e.printStackTrace();
			}
			
			

			return user;
		} else {
			return null;
		}
	}

	/**
	 * Returns the user stored in database
	 * 
	 * @return
	 */
	public User getUser() {
		Cursor cursor = getDatabase().query(DatabaseHelper.TABLE_USER, USER_COLLUMNS, null, null, null, null, null);

		User fetchedUser = cursorToUser(cursor);
		cursor.close();

		if (fetchedUser == null) {
			return null;
		}

		// get units from user
		List<Unit> allUnits = getUnitsFromUser(fetchedUser);
		for (Unit unit : allUnits) {
			fetchedUser.addUnit(unit);
		}

		// TODO get progress from user

		return fetchedUser;
	}

	/**
	 * Deletes user from database.
	 */
	public void deleteUser() {
		getDatabase().delete(DatabaseHelper.TABLE_USER, null, null);

		// delete units
		deleteAllUnits();

		// delete the progress data
		deleteAllProgressData();
	}

	/**
	 * Deletes all worksets from database.
	 */
	public void deleteAllWorksets() {
		getDatabase().delete(DatabaseHelper.TABLE_WORKSETS, null, null);

	}

	/**
	 * Returns worksets from a unit.
	 * 
	 * @param unit
	 * @return
	 */
	public List<Workset> getWorksets(Unit unit) {
		List<Workset> worksets = new ArrayList<Workset>();

		Cursor cursor = getDatabase().query(DatabaseHelper.TABLE_WORKSETS, WORKSET_COLLUMNS, DatabaseHelper.WORKSETS_COLUMN_ID_UNIT + " = " + unit.getId(), null, null, null, null);

		while (!cursor.isAfterLast()) {
			Workset workset = cursorToWorkSet(cursor);
			if (workset != null) {
				worksets.add(workset);
			}

			cursor.moveToNext();
		}
		// make sure to close the cursor
		cursor.close();
		return worksets;
	}

	/**
	 * Persists a workset. Returns persisted workset.
	 * 
	 * Returns null if something bad happened
	 * 
	 * @param workSet
	 * @param unit
	 * @return
	 */
	public Workset createWorkSet(Workset workSet, Unit unit) {
		ContentValues values = new ContentValues();

		values.put(DatabaseHelper.WORKSETS_COLUMN_COUNT, workSet.getCount());

		if (unit.getId() != 0) {
			values.put(DatabaseHelper.WORKSETS_COLUMN_ID_UNIT, unit.getId());
		} else {
			Log.e(TAG, "Can't create workset. No id_unit given");
			return null;
		}

		if (workSet.getTodo() != 0) {
			values.put(DatabaseHelper.WORKSETS_COLUMN_TODO, workSet.getTodo());
		} else {
			Log.e(TAG, "Can't create workset. No todo given");
			return null;
		}

		long insertId = getDatabase().insert(DatabaseHelper.TABLE_WORKSETS, null, values);
		if (insertId >= 0) {
			// successfull insertion
			Cursor cursor = getDatabase().query(DatabaseHelper.TABLE_WORKSETS, WORKSET_COLLUMNS, DatabaseHelper.WORKSETS_COLUMN_ID + " = " + insertId, null, null, null, null);

			Workset fetchedWorkset = cursorToWorkSet(cursor);
			cursor.close();

			return fetchedWorkset;
		} else {
			Log.e(TAG, "Could  not insert workset to database");
			return null;
		}
	}

	/**
	 * Converts cursor to workset instance.
	 * 
	 * @param cursor
	 * @return
	 */
	private Workset cursorToWorkSet(Cursor cursor) {
		if (cursor.moveToFirst()) {
			Workset workSet = new Workset();
			workSet.setId(cursor.getLong(0));
			workSet.setCount(cursor.getInt(1));
			workSet.setTodo(cursor.getInt(2));

			return workSet;
		} else {
			return null;
		}
	}

	/**
	 * Converts cursor to progress instance.
	 * 
	 * @param cursor
	 * @return
	 */
	private Progress cursorToProgress(Cursor cursor) {
		if (cursor.moveToFirst()) {
			Progress progress = new Progress();
			progress.setId(cursor.getLong(0));

			String dateAsString = cursor.getString(1);
			Date creationDate;

			try {
				creationDate = datetimeFormat.parse(dateAsString);
				progress.setCreationDate(creationDate);
			} catch (ParseException e) {
				e.printStackTrace();
			}

			progress.setAge(cursor.getInt(2));
			progress.setWeight(cursor.getDouble(3));

			return progress;
		} else {
			return null;
		}
	}

}
