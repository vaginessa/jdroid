package com.jdroid.android.sample.ui.firebase;

import com.jdroid.android.firebase.FirebaseAppContext;
import com.jdroid.android.firebase.FirebaseAppModule;

public class AndroidFirebaseAppModule extends FirebaseAppModule {

	@Override
	protected FirebaseAppContext createFirebaseAppContext() {
		return new AndroidFirebaseAppContext();
	}
}
