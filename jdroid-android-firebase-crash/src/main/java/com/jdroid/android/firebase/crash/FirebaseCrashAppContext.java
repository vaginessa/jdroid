package com.jdroid.android.firebase.crash;

import com.jdroid.android.analytics.AnalyticsTracker;
import com.jdroid.android.context.AbstractAppContext;

public class FirebaseCrashAppContext extends AbstractAppContext {

	public AnalyticsTracker getAnalyticsTracker() {
		return FirebaseCrashTracker.get();
	}
}
