package com.jdroid.android.firebase.analytics;

import android.support.annotation.WorkerThread;

import com.jdroid.android.firebase.FirebaseAppModule;
import com.jdroid.java.analytics.AnalyticsTracker;

public class AbstractFirebaseAnalyticsTracker implements AnalyticsTracker {

	@Override
	public Boolean isEnabled() {
		return FirebaseAppModule.get().getFirebaseAppContext().isFirebaseAnalyticsEnabled();
	}

	@WorkerThread
	protected FirebaseAnalyticsHelper getFirebaseAnalyticsHelper() {
		return FirebaseAppModule.get().getFirebaseAnalyticsHelper();
	}
}
