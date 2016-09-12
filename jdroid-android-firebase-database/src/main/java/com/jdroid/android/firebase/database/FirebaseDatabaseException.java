package com.jdroid.android.firebase.database;

import com.google.firebase.database.DatabaseError;
import com.jdroid.java.exception.AbstractException;

public class FirebaseDatabaseException extends AbstractException {

	private DatabaseError databaseError;

	public FirebaseDatabaseException(DatabaseError databaseError) {
		super(databaseError.getMessage());
		this.databaseError = databaseError;
	}

	public DatabaseError getDatabaseError() {
		return databaseError;
	}
}
