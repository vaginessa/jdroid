package com.jdroid.android.application;

import android.app.Activity;

public abstract class AbstractActivityLifecycleListener implements ActivityLifecycleListener {

	@Override
	public void onCreateActivity(Activity activity) {
		// Do nothing
	}

	@Override
	public void onStartActivity(Activity activity) {
		// Do nothing
	}

	@Override
	public void onResumeActivity(Activity activity) {
		// Do nothing
	}

	@Override
	public void onPauseActivity(Activity activity) {
		// Do nothing
	}

	@Override
	public void onStopActivity(Activity activity) {
		// Do nothing
	}

	@Override
	public void onDestroyActivity(Activity activity) {
		// Do nothing
	}
}
