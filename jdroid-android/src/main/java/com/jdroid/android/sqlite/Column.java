package com.jdroid.android.sqlite;

import android.content.ContentValues;
import android.database.Cursor;

/**
 * 
 * @author Maxi Rosson
 * @param <T>
 */
public interface Column<T> {
	
	public void addValue(ContentValues values, T item);
	
	public <E> E readValue(Cursor cursor);
	
	/**
	 * @return the dataType
	 */
	public DataType getDataType();
	
	/**
	 * @return the columnName
	 */
	public String getColumnName();
	
	public String getExtraQualifier();
	
	/**
	 * @return the optional
	 */
	public Boolean isOptional();
}
