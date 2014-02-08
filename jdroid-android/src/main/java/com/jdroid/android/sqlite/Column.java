package com.jdroid.android.sqlite;

import android.content.ContentValues;
import android.database.Cursor;

/**
 * Represents a column definition.
 * 
 * @author Maxi Rosson
 */
public interface Column {
	
	/**
	 * Default id column name.
	 */
	public final String ID = "id";
	
	/**
	 * Default parent id column name.
	 */
	public final String PARENT_ID = "parentId";
	
	/**
	 * Primary key constraint
	 */
	public final String PRIMARY_KEY = "PRIMARY KEY";
	
	/**
	 * Adds a the value given value to {@link ContentValues} according column definition.
	 * 
	 * @param values to add the value.
	 * @param value to add.
	 */
	public <T> void addValue(ContentValues values, T value);
	
	/**
	 * Reads the value from {@link Cursor} according column definition.
	 * 
	 * @param cursor to get the data.
	 * @return the value.
	 */
	public <E> E readValue(Cursor cursor);
	
	/**
	 * @return the dataType
	 */
	public DataType getDataType();
	
	/**
	 * @return the columnName
	 */
	public String getColumnName();
	
	/**
	 * @return extra qualifiers for the column, like "PRIMARY KEY" or "AUTOINCREMENT".
	 */
	public String getExtraQualifier();
	
	/**
	 * @return the optional
	 */
	public Boolean isOptional();
	
	/**
	 * @return true if the column should be included in unique constraint.
	 */
	public Boolean isUnique();
	
	/**
	 * Returns a reference if the column contains a foreign key and should be added to reference constraints, otherwise
	 * returns null.
	 * 
	 * @return the reference.
	 */
	public Reference getReference();
}
