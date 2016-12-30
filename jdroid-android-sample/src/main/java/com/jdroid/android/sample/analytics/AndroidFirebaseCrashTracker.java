package com.jdroid.android.sample.analytics;

import com.jdroid.android.firebase.crash.FirebaseCrashTracker;

import java.util.List;

public class AndroidFirebaseCrashTracker extends DefaultAnalyticsTracker {
	
	@Override
	public Boolean isEnabled() {
		return FirebaseCrashTracker.get().isEnabled();
	}
	
	@Override
	public void trackHandledException(Throwable throwable, List<String> tags) {
		FirebaseCrashTracker.get().trackHandledException(throwable, tags);
	}

	@Override
	public void trackErrorBreadcrumb(String message) {
		FirebaseCrashTracker.get().trackErrorBreadcrumb(message);
	}
}