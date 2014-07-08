package de.uks.challenger.persistence;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import de.uks.challenger.model.Unit;
import de.uks.challenger.model.User;
import de.uks.challenger.model.User.GENDER;
import de.uks.challenger.model.Workset;

public class Source {
	private static final String TAG = "Challenger.Source";

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
	 * @param unit
	 * @return
	 */
	public Unit createUnit(Unit unit, User user) {
		ContentValues values = new ContentValues();

		// format the date
		if (unit.getCreationDate() != null) {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String formattedDate = formatter.format(unit.getCreationDate());
			values.put(DatabaseHelper.UNITS_COLUMN_CREATION_DATE, formattedDate);
		} else {
			Log.e(TAG, "Can't create unit. No creation date given");
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

			String[] collumns = { DatabaseHelper.UNITS_COLUMN_ID, DatabaseHelper.UNITS_COLUMN_CREATION_DATE };
			Cursor cursor = getDatabase().query(DatabaseHelper.TABLE_UNITS, collumns, DatabaseHelper.UNITS_COLUMN_ID + " = " + insertId, null, null, null, null);

			cursor.moveToFirst();
			Unit fetchedUnit = curserToUnit(cursor);
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
				}
			}

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

		String[] collumns = { DatabaseHelper.UNITS_COLUMN_ID, DatabaseHelper.UNITS_COLUMN_CREATION_DATE };
		Cursor cursor = getDatabase().query(DatabaseHelper.TABLE_UNITS, collumns, DatabaseHelper.UNITS_COLUMN_ID_USER + " = " + user.getId(), null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Unit unit = curserToUnit(cursor);

			// get worksets to the unit
			List<Workset> worksets = getWorksets(unit);
			for (Workset workset : worksets) {
				unit.addWorkset(workset);
			}

			units.add(unit);

			cursor.moveToNext();
		}
		// make sure to close the cursor
		cursor.close();
		return units;
	}

	/**
	 * Returns all stored units.
	 * 
	 * @return
	 */
	public List<Unit> getAllUnits() {
		List<Unit> units = new ArrayList<Unit>();

		String[] collumns = { DatabaseHelper.UNITS_COLUMN_ID, DatabaseHelper.UNITS_COLUMN_CREATION_DATE };
		Cursor cursor = getDatabase().query(DatabaseHelper.TABLE_UNITS, collumns, null, null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Unit unit = curserToUnit(cursor);

			// get worksets to the unit
			List<Workset> worksets = getWorksets(unit);
			for (Workset workset : worksets) {
				unit.addWorkset(workset);
			}

			units.add(unit);

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
	private Unit curserToUnit(Cursor cursor) {
		Unit unit = new Unit();
		unit.setId(cursor.getLong(0));

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			unit.setCreationDate(formatter.parse(cursor.getString(1)));
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return unit;

	}

	/**
	 * Returns
	 * 
	 * @param user
	 * @return
	 */
	public User createUser(User user) {

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

		long insertId = getDatabase().insert(DatabaseHelper.TABLE_USER, null, values);
		if (insertId >= 0) {
			// successfull insertion
			String[] collumns = { DatabaseHelper.USER_COLUMN_ID, DatabaseHelper.USER_COLUMN_GENDER, DatabaseHelper.USER_COLUMN_HEIGHT };
			Cursor cursor = getDatabase().query(DatabaseHelper.TABLE_USER, collumns, DatabaseHelper.USER_COLUMN_ID + " = " + insertId, null, null, null, null);

			cursor.moveToFirst();
			User fetchedUser = cursorToUser(cursor);
			cursor.close();

			// create units from user
			for (Iterator<Unit> it = user.getUnitIterator(); it.hasNext();) {
				Unit unit = it.next();

				createUnit(unit, fetchedUser);
			}

			// TODO create progress data

			return fetchedUser;
		} else {
			Log.e(TAG, "Coul'd not insert user to database.");
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
		User user = new User();
		user.setId(cursor.getLong(0));

		if (0 == cursor.getInt(1)) {
			user.setGender(GENDER.MALE);
		} else {
			user.setGender(GENDER.FEMALE);
		}

		user.setHeight(cursor.getInt(2));

		return user;
	}

	/**
	 * Returns the user stored in database
	 * 
	 * @return
	 */
	public User getUser() {
		String[] collumns = { DatabaseHelper.USER_COLUMN_ID, DatabaseHelper.USER_COLUMN_GENDER, DatabaseHelper.USER_COLUMN_HEIGHT };
		Cursor cursor = getDatabase().query(DatabaseHelper.TABLE_USER, collumns, null, null, null, null, null);

		cursor.moveToFirst();
		User fetchedUser = cursorToUser(cursor);
		cursor.close();

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

		// TODO delete progress
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

		String[] collumns = { DatabaseHelper.WORKSETS_COLUMN_ID, DatabaseHelper.WORKSETS_COLUMN_COUNT };
		Cursor cursor = getDatabase().query(DatabaseHelper.TABLE_WORKSETS, collumns, DatabaseHelper.WORKSETS_COLUMN_ID_UNIT + " = " + unit.getId(), null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Workset workset = cursorToWorkSet(cursor);

			worksets.add(workset);

			cursor.moveToNext();
		}
		// make sure to close the cursor
		cursor.close();
		return worksets;
	}

	/**
	 * Persists a workset. Returns persisted workset.
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
		}else{
			Log.e(TAG, "Can't create workset. No id_unit given");
			return null;
		}

		long insertId = getDatabase().insert(DatabaseHelper.TABLE_WORKSETS, null, values);
		if (insertId >= 0) {
			// successfull insertion
			String[] collumns = { DatabaseHelper.WORKSETS_COLUMN_ID, DatabaseHelper.WORKSETS_COLUMN_COUNT };
			Cursor cursor = getDatabase().query(DatabaseHelper.TABLE_WORKSETS, collumns, DatabaseHelper.WORKSETS_COLUMN_ID + " = " + insertId, null, null, null, null);

			cursor.moveToFirst();

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
	public Workset cursorToWorkSet(Cursor cursor) {
		Workset workSet = new Workset();
		workSet.setId(cursor.getLong(0));
		workSet.setCount(cursor.getInt(1));

		return workSet;
	}

}
