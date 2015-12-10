package com.jdroid.javaweb.sample.context;

import com.jdroid.javaweb.context.AppContext;

/**
 * The application context
 */
public class ServerAppContext extends AppContext {
	
	private String firebaseUrl;
	private String firebaseAuthToken;

	public String getFirebaseUrl() {
		return firebaseUrl;
	}

	public void setFirebaseUrl(String firebaseUrl) {
		this.firebaseUrl = firebaseUrl;
	}

	public String getFirebaseAuthToken() {
		return firebaseAuthToken;
	}

	public void setFirebaseAuthToken(String firebaseAuthToken) {
		this.firebaseAuthToken = firebaseAuthToken;
	}
}
