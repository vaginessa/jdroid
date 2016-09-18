package com.jdroid.android.firebase.remoteconfig;

import com.jdroid.android.debug.PreferencesAppender;
import com.jdroid.java.collections.Lists;

import java.util.List;

public class FirebaseRemoteConfigDebugContext {

	public List<PreferencesAppender> getPreferencesAppenders() {
		List<PreferencesAppender> appenders = Lists.newArrayList();
		appenders.add(createFirebaseRemoteConfigPrefsAppender());
		return appenders;
	}

	protected FirebaseRemoteConfigPrefsAppender createFirebaseRemoteConfigPrefsAppender() {
		return new FirebaseRemoteConfigPrefsAppender();
	}
}
