package com.jdroid.android.sample.exception;

import com.jdroid.android.analytics.AnalyticsTracker;
import com.jdroid.android.firebase.crash.FirebaseCrashAppContext;
import com.jdroid.android.sample.analytics.AndroidFirebaseCrashTracker;

public class AndroidFirebaseCrashAppContext extends FirebaseCrashAppContext {

	@Override
	public AnalyticsTracker getAnalyticsTracker() {
		return new AndroidFirebaseCrashTracker();
	}
}
