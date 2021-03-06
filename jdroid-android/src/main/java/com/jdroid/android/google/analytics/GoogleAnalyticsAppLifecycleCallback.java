package com.jdroid.android.google.analytics;

import android.content.Context;

import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.lifecycle.ApplicationLifecycleCallback;

public class GoogleAnalyticsAppLifecycleCallback extends ApplicationLifecycleCallback {
	@Override
	public void onProviderInit(Context context) {
		if (GoogleAnalyticsAppContext.isGoogleAnalyticsEnabled()) {
			AbstractApplication.get().addCoreAnalyticsTracker(new GoogleCoreAnalyticsTracker());
		}
	}
}
