package com.jdroid.android.sample.ui.firebase.remoteconfig;

import com.jdroid.android.firebase.remoteconfig.FirebaseRemoteConfigAppContext;
import com.jdroid.android.firebase.remoteconfig.FirebaseRemoteConfigAppModule;

public class AndroidFirebaseRemoteConfigAppModule extends FirebaseRemoteConfigAppModule {

	@Override
	protected FirebaseRemoteConfigAppContext createFirebaseRemoteConfigAppContext() {
		return new AndroidFirebaseRemoteConfigAppContext();
	}
}
