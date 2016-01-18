package com.jdroid.android.application;

import android.app.Activity;

public interface ActivityLifecycleListener {

	public void onCreateActivity(Activity activity);

	public void onStartActivity(Activity activity);

	public void onResumeActivity(Activity activity);

	public void onPauseActivity(Activity activity);

	public void onStopActivity(Activity activity);

	public void onDestroyActivity(Activity activity);
}
