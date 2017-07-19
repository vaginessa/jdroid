package com.jdroid.android.firebase.remoteconfig;

import android.content.Context;

import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.lifecycle.ApplicationLifecycleCallback;

public class FirebaseRemoteConfigAppLifecycleCallback extends ApplicationLifecycleCallback {
	
	@Override
	public void onProviderInit(Context context) {
		AbstractApplication.get().addCoreAnalyticsTracker(new FirebaseRemoteConfigTracker());
	}
}
