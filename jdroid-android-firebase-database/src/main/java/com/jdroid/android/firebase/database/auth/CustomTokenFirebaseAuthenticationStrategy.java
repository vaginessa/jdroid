package com.jdroid.android.firebase.database.auth;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public abstract class CustomTokenFirebaseAuthenticationStrategy extends FirebaseAuthenticationStrategy {

	@Override
	protected void doAuthenticate(DatabaseReference databaseReference) {
		FirebaseAuth.getInstance().addAuthStateListener(this);
		FirebaseAuth.getInstance().signInWithCustomToken(getAuthToken()).addOnFailureListener(this);
	}

	protected abstract String getAuthToken();
}
