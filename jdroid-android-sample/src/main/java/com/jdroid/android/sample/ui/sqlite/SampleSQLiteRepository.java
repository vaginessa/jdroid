package com.jdroid.android.sample.ui.sqlite;

import android.content.ContentValues;
import android.database.Cursor;

import com.jdroid.android.sqlite.Column;
import com.jdroid.android.sqlite.SQLiteHelper;
import com.jdroid.android.sqlite.repository.SQLiteRepository;

public class SampleSQLiteRepository extends SQLiteRepository<SampleSQLiteEntity> {

	public SampleSQLiteRepository(SQLiteHelper dbHelper) {
		super(dbHelper);
	}

	@Override
	protected String getTableName() {
		return SampleSQLiteEntity.class.getSimpleName();
	}

	@Override
	protected Column[] getColumns() {
		return SampleSQLiteEntityColumns.values();
	}

	@Override
	protected SampleSQLiteEntity createObjectFromCursor(Cursor cursor) {
		SampleSQLiteEntity entity = new SampleSQLiteEntity();
		entity.setId(SampleSQLiteEntityColumns.ID.<Long>readValue(cursor));
		entity.setField(SampleSQLiteEntityColumns.FIELD.<String>readValue(cursor));
		return entity;
	}

	@Override
	protected ContentValues createContentValuesFromObject(SampleSQLiteEntity item) {
		ContentValues values = new ContentValues();
		SampleSQLiteEntityColumns.ID.addValue(values, item.getId());
		SampleSQLiteEntityColumns.FIELD.addValue(values, item.getField());
		return values;
	}
}
