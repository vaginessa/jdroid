package com.jdroid.android.firebase.analytics;

import android.content.Context;

import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.lifecycle.ApplicationLifecycleCallback;

public class FirebaseAnalyticsAppLifecycleCallback extends ApplicationLifecycleCallback {
	@Override
	public void onProviderInit(Context context) {
		if (FirebaseAnalyticsAppContext.isFirebaseAnalyticsEnabled()) {
			AbstractApplication.get().addCoreAnalyticsTracker(new FirebaseCoreAnalyticsTracker());
		}
	}
}
