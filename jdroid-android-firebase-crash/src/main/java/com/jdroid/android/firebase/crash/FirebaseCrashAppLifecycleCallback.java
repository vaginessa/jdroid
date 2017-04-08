package com.jdroid.android.firebase.crash;

import android.content.Context;

import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.application.lifecycle.ApplicationLifecycleCallback;

public class FirebaseCrashAppLifecycleCallback extends ApplicationLifecycleCallback {
	
	@Override
	public void onProviderInit(Context context) {
		AbstractApplication.get().addCoreAnalyticsTracker(new FirebaseCrashTracker());
	}
}
