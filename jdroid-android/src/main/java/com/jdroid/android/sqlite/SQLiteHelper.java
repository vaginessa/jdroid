package com.jdroid.android.sqlite;

import java.util.List;
import java.util.Set;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.jdroid.android.utils.AndroidUtils;
import com.jdroid.java.collections.Lists;
import com.jdroid.java.collections.Sets;

public class SQLiteHelper extends SQLiteOpenHelper {
	
	private static final String DB_NAME = "application.db";
	
	private Set<String> createSQLs = Sets.newHashSet();
	private List<SQLiteUpgradeStep> upgradeSteps = Lists.newArrayList();
	
	public SQLiteHelper(Context context) {
		super(context, DB_NAME, null, AndroidUtils.getVersionCode());
	}
	
	/**
	 * Add a creation SQL statement to be executed in {@link SQLiteHelper#onCreate(SQLiteDatabase)} method.
	 * 
	 * @param sql creation statement
	 */
	public void addCreateSQL(String sql) {
		if (sql != null) {
			createSQLs.add(sql);
		}
	}
	
	/**
	 * Add a list of {@link SQLiteUpgradeStep} to be executed in
	 * {@link SQLiteHelper#onUpgrade(SQLiteDatabase, int, int)} method.
	 * 
	 * @param upgradeSteps list of upgrade steps to add.
	 */
	public void addUpgradeSteps(List<SQLiteUpgradeStep> upgradeSteps) {
		this.upgradeSteps.addAll(upgradeSteps);
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
		for (SQLiteUpgradeStep upgradeStep : upgradeSteps) {
			if (upgradeStep.getVersion() > oldVersion) {
				upgradeStep.upgrade(db);
			}
		}
	}
	
	/**
	 * @see android.database.sqlite.SQLiteOpenHelper#onOpen(android.database.sqlite.SQLiteDatabase)
	 */
	@Override
	public void onOpen(SQLiteDatabase db) {
		super.onOpen(db);
		if (!db.isReadOnly()) {
			// Enable foreign key constraints support
			db.execSQL("PRAGMA foreign_keys=ON;");
		}
	}
}
