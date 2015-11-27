package com.jdroid.android.sample.ui.sqlite;

import android.content.ContentValues;
import android.database.Cursor;

import com.jdroid.android.sqlite.Column;
import com.jdroid.android.sqlite.DataType;
import com.jdroid.android.sqlite.Reference;

public enum SampleSQLiteEntityColumns implements Column {

	ID(Column.ID, DataType.LONG, Column.PRIMARY_KEY, Boolean.FALSE, Boolean.TRUE),
	FIELD("field", DataType.TEXT, null, Boolean.FALSE, Boolean.FALSE);

	private String columnName;
	private DataType dataType;
	private String extraQualifier;
	private Boolean optional;
	private Boolean unique;

	private SampleSQLiteEntityColumns(String columnName, DataType dataType, String extraQualifier, Boolean optional,
									  Boolean unique) {
		this.columnName = columnName;
		this.dataType = dataType;
		this.extraQualifier = extraQualifier;
		this.optional = optional;
		this.unique = unique;
	}

	@Override
	public <T> void addValue(ContentValues values, T value) {
		dataType.writeValue(values, columnName, value);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <E> E readValue(Cursor cursor) {
		return (E)dataType.readValue(cursor, columnName);
	}

	@Override
	public DataType getDataType() {
		return dataType;
	}

	@Override
	public String getColumnName() {
		return columnName;
	}

	@Override
	public String getExtraQualifier() {
		return extraQualifier;
	}

	@Override
	public Boolean isOptional() {
		return optional;
	}

	@Override
	public Boolean isUnique() {
		return unique;
	}

	@Override
	public Reference getReference() {
		return null;
	}
}