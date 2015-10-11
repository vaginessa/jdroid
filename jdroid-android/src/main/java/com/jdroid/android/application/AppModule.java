package com.jdroid.android.application;

import android.content.Context;
import android.content.res.Configuration;

import com.jdroid.android.analytics.AnalyticsTracker;
import com.jdroid.android.debug.PreferencesAppender;

import java.util.List;

public interface AppModule {

	public void onCreate();

	public void onConfigurationChanged(Configuration newConfig);

	public void onLowMemory();

	public void onTrimMemory(int level);

	public void attachBaseContext(Context base);

	public List<? extends AnalyticsTracker> getAnalyticsTrackers();

	public List<PreferencesAppender> getPreferencesAppenders();
}
