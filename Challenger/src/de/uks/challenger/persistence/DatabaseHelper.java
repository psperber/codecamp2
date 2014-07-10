package de.uks.challenger.persistence;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Class for creating the sql lite tables
 * 
 * @author philipp
 * 
 */
public class DatabaseHelper extends SQLiteOpenHelper {
	// table for units
	public static final String TABLE_UNITS = "units";
	public static final String UNITS_COLUMN_ID = "id";
	public static final String UNITS_COLUMN_ID_USER = "id_user";
	public static final String UNITS_COLUMN_UNIT_TYPE = "unit_type";

	public static final String UNITS_COLUMN_CREATION_DATE = "creation_date";

	// table for worksets
	public static final String TABLE_WORKSETS = "worksets";
	public static final String WORKSETS_COLUMN_ID = "id";
	public static final String WORKSETS_COLUMN_ID_UNIT = "id_unit";
	public static final String WORKSETS_COLUMN_COUNT = "count";
	public static final String WORKSETS_COLUMN_TODO = "todo";

	// table for user progress
	public static final String TABLE_PROGRESS = "progress";
	public static final String PROGRESS_COLUMN_ID = "id";
	public static final String PROGRESS_COLUMN_CREATION_DATE = "creation_date";
	public static final String PROGRESS_COLUMN_AGE = "age";
	public static final String PROGRESS_COLUMN_WEIGHT = "weight";
	public static final String PROGRESS_COLUMN_ID_USER = "id_user";

	// table for user data
	public static final String TABLE_USER = "user";
	public static final String USER_COLUMN_ID = "id";
	public static final String USER_COLUMN_GENDER = "gender";
	public static final String USER_COLUMN_HEIGHT = "height";
	public static final String USER_COLUMN_RESTING_TIME = "resting_time";
	public static final String USER_COLUMN_BIRTHDAY = "birthday";

	private static final String DATABASE_NAME = "challenger.db";
	private static final int DATABASE_VERSION = 14;

	private static final String DATABASE_UNITS_CREATE = "create table " + TABLE_UNITS + "(" + UNITS_COLUMN_ID + " integer primary key autoincrement, " + UNITS_COLUMN_ID_USER + " integer not null, " + UNITS_COLUMN_CREATION_DATE
			+ " datetime not null, " + UNITS_COLUMN_UNIT_TYPE + " integer);";
	private static final String DATABASE_WORKSETS_CREATE = "create table " + TABLE_WORKSETS + "(" + WORKSETS_COLUMN_ID + " integer primary key autoincrement, " + WORKSETS_COLUMN_ID_UNIT + " integer not null, " + WORKSETS_COLUMN_COUNT + " integer, "
			+ WORKSETS_COLUMN_TODO + " integer);";
	private static final String DATABASE_PROGRESS_CREATE = "create table " + TABLE_PROGRESS + "(" + PROGRESS_COLUMN_ID + " integer primary key autoincrement, " + PROGRESS_COLUMN_CREATION_DATE + " datetime not null, " + PROGRESS_COLUMN_AGE
			+ " integer not null,  " + PROGRESS_COLUMN_WEIGHT + " double not null, " + PROGRESS_COLUMN_ID_USER + " integer);";
	private static final String DATABASE_USER_CREATE = "create table " + TABLE_USER + "(" + USER_COLUMN_ID + " integer primary key autoincrement, " + USER_COLUMN_GENDER + " integer not null, " + USER_COLUMN_HEIGHT + " integer not null, "
			+ USER_COLUMN_RESTING_TIME + " integer, " + USER_COLUMN_BIRTHDAY + " datetime not null);";

	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(DATABASE_UNITS_CREATE);
		db.execSQL(DATABASE_WORKSETS_CREATE);
		db.execSQL(DATABASE_PROGRESS_CREATE);
		db.execSQL(DATABASE_USER_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(DatabaseHelper.class.getName(), "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");

		// remove old stuff
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_UNITS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_WORKSETS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROGRESS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);

		// create new table
		onCreate(db);
	}

}
