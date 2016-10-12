package com.jdroid.android.firebase.analytics;

import android.support.annotation.WorkerThread;

import com.jdroid.android.firebase.FirebaseAppModule;
import com.jdroid.java.analytics.BaseAnalyticsTracker;

public class AbstractFirebaseAnalyticsTracker implements BaseAnalyticsTracker {

	@Override
	public Boolean isEnabled() {
		return FirebaseAppModule.get().getFirebaseAppContext().isFirebaseAnalyticsEnabled();
	}

	@WorkerThread
	protected FirebaseAnalyticsHelper getFirebaseAnalyticsHelper() {
		return FirebaseAppModule.get().getFirebaseAnalyticsHelper();
	}
}
