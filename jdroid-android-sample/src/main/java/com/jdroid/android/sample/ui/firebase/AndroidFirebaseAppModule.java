package com.jdroid.android.sample.ui.firebase;

import com.jdroid.android.google.firebase.FirebaseAppContext;
import com.jdroid.android.google.firebase.FirebaseAppModule;

public class AndroidFirebaseAppModule extends FirebaseAppModule {

	@Override
	protected FirebaseAppContext createFirebaseAppContext() {
		return new AndroidFirebaseAppContext();
	}
}
