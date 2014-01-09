package com.jdroid.android.sqlite;

/**
 * 
 * @author Maxi Rosson
 */
public enum DataType {
	TEXT("text"),
	INTEGER("integer"),
	REAL("real"),
	BLOB("blob");
	
	private String type;
	
	private DataType(String type) {
		this.type = type;
	}
	
	public String getType() {
		return type;
	}
}
