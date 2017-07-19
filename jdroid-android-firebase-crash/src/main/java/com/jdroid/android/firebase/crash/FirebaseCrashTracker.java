package com.jdroid.android.firebase.crash;

import android.app.Activity;
import android.os.Bundle;

import com.google.firebase.crash.FirebaseCrash;
import com.jdroid.android.analytics.AbstractCoreAnalyticsTracker;
import com.jdroid.android.exception.DefaultExceptionHandler;
import com.jdroid.android.uri.ReferrerUtils;

import java.util.List;

public class FirebaseCrashTracker extends AbstractCoreAnalyticsTracker {
	
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

	@Override
	public void onActivityCreate(Activity activity, Bundle savedInstanceState) {
		super.onActivityCreate(activity, savedInstanceState);
		FirebaseCrash.log(activity.getClass().getSimpleName() + " created. SavedInstanceState " + (savedInstanceState != null ? "not null" : "null"));
	}

	@Override
	public void onActivityStart(Activity activity, String referrer, Object data) {
		FirebaseCrash.log(activity.getClass().getSimpleName() + " started");
		if (!ReferrerUtils.isUndefined(referrer)) {
			FirebaseCrash.log("Referrer: " + referrer);
		}
	}

	@Override
	public void onActivityResume(Activity activity) {
		FirebaseCrash.log(activity.getClass().getSimpleName() + " resumed");
	}

	@Override
	public void onActivityPause(Activity activity) {
		FirebaseCrash.log(activity.getClass().getSimpleName() + " paused");
	}

	@Override
	public void onActivityStop(Activity activity) {
		FirebaseCrash.log(activity.getClass().getSimpleName() + " stopped");
	}

	@Override
	public void onActivityDestroy(Activity activity) {
		FirebaseCrash.log(activity.getClass().getSimpleName() + " finished");
	}
}