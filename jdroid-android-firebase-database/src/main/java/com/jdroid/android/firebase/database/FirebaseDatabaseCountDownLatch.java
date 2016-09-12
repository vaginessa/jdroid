package com.jdroid.android.firebase.database;


import com.google.firebase.database.DataSnapshot;

import java.util.concurrent.CountDownLatch;

public class FirebaseDatabaseCountDownLatch extends CountDownLatch {

	private DataSnapshot dataSnapshot;
	private FirebaseDatabaseException firebaseDatabaseException;

	public FirebaseDatabaseCountDownLatch() {
		super(1);
	}

	public FirebaseDatabaseException getFirebaseDatabaseException() {
		return firebaseDatabaseException;
	}

	public void setFirebaseDatabaseException(FirebaseDatabaseException firebaseDatabaseException) {
		this.firebaseDatabaseException = firebaseDatabaseException;
	}

	public DataSnapshot getDataSnapshot() {
		return dataSnapshot;
	}

	public void setDataSnapshot(DataSnapshot dataSnapshot) {
		this.dataSnapshot = dataSnapshot;
	}
}
