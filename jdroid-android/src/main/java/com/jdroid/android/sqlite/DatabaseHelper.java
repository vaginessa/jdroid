package com.jdroid.android.sqlite;

import java.util.Set;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import com.jdroid.android.AbstractApplication;
import com.jdroid.java.collections.Sets;

public class DatabaseHelper extends SQLiteOpenHelper {
	
	private static final String TAG = DatabaseHelper.class.getSimpleName();
	
	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "database";
	
	private static final DatabaseHelper INSTANCE = new DatabaseHelper();
	private Set<String> createSQLs = Sets.newHashSet();
	private Set<String> dropSQLs = Sets.newHashSet();
	
	public static DatabaseHelper get() {
		return INSTANCE;
	}
	
	private DatabaseHelper() {
		super(AbstractApplication.get(), DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	public void addCreateSQL(String sql) {
		createSQLs.add(sql);
	}
	
	public void addDropSQL(String sql) {
		dropSQLs.add(sql);
	}
	
	/**
	 * @see android.database.sqlite.SQLiteOpenHelper#onCreate(android.database.sqlite.SQLiteDatabase)
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		for (String createSQL : createSQLs) {
			db.execSQL(createSQL);
		}
	}
	
	/**
	 * @see android.database.sqlite.SQLiteOpenHelper#onUpgrade(android.database.sqlite.SQLiteDatabase, int, int)
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(TAG, "Upgrading database from version " + oldVersion + " to " + newVersion
				+ ", which will destroy all old data");
		for (String dropSQL : dropSQLs) {
			db.execSQL(dropSQL);
		}
		onCreate(db);
	}
}