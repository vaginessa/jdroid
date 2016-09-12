package com.jdroid.android.firebase.database;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.jdroid.java.exception.UnexpectedException;

public class FirebaseDatabaseCompletionListener implements DatabaseReference.CompletionListener {

	private FirebaseDatabaseCountDownLatch done = new FirebaseDatabaseCountDownLatch();

	@Override
	public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
		if (databaseError != null) {
			done.setFirebaseDatabaseException(new FirebaseDatabaseException(databaseError));
		}
		done.countDown();
	}

	public void waitOperation() {
		try {
			done.await();
			if (done.getFirebaseDatabaseException() != null) {
				throw done.getFirebaseDatabaseException();
			}
		} catch (InterruptedException e) {
			throw new UnexpectedException(e);
		}
	}
}
