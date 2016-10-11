package com.jdroid.android.sample.firebase;

import com.jdroid.android.analytics.AnalyticsTracker;
import com.jdroid.android.firebase.FirebaseAppModule;
import com.jdroid.android.sample.analytics.AndroidFirebaseAnalyticsTracker;

public class AndroidFirebaseAppModule extends FirebaseAppModule {

	@Override
	protected AnalyticsTracker createFirebaseAnalyticsTracker() {
		return new AndroidFirebaseAnalyticsTracker();
	}
}
