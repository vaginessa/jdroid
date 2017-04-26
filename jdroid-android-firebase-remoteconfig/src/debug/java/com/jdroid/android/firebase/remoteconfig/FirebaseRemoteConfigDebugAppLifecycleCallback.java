package com.jdroid.android.firebase.remoteconfig;

import android.content.Context;

import com.jdroid.android.lifecycle.ApplicationLifecycleCallback;
import com.jdroid.android.debug.DebugSettingsHelper;

public class FirebaseRemoteConfigDebugAppLifecycleCallback extends ApplicationLifecycleCallback {
	
	@Override
	public void onProviderInit(Context context) {
		DebugSettingsHelper.addPreferencesAppender(new FirebaseRemoteConfigPrefsAppender());
	}
	
}
