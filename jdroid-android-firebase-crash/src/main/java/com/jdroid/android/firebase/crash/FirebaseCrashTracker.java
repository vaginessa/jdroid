package com.jdroid.android.firebase.crash;

import com.google.firebase.crash.FirebaseCrash;
import com.jdroid.android.analytics.AbstractCoreAnalyticsTracker;
import com.jdroid.android.exception.DefaultExceptionHandler;

import java.util.List;

public class FirebaseCrashTracker extends AbstractCoreAnalyticsTracker {
	
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