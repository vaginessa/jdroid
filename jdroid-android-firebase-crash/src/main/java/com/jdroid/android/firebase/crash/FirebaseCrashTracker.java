package com.jdroid.android.firebase.crash;

import com.google.firebase.crash.FirebaseCrash;
import com.jdroid.android.analytics.AbstractAnalyticsTracker;
import com.jdroid.android.exception.DefaultExceptionHandler;

import java.util.List;

public class FirebaseCrashTracker extends AbstractAnalyticsTracker {
	
	private static final FirebaseCrashTracker INSTANCE = new FirebaseCrashTracker();
	
	public static FirebaseCrashTracker get() {
		return INSTANCE;
	}
	
	@Override
	public Boolean isEnabled() {
		return true;
	}
	
	@Override
	public void trackHandledException(Throwable throwable, List<String> tags) {
		if (areTagsEnabled()) {
			DefaultExceptionHandler.addTags(throwable, tags);
		}
		FirebaseCrash.report(throwable);
	}

	protected Boolean areTagsEnabled() {
		return false;
	}

	@Override
	public void trackErrorBreadcrumb(String message) {
		FirebaseCrash.log(message);
	}
}