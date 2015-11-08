package com.jdroid.android.application;

import android.content.Context;
import android.content.res.Configuration;

import com.jdroid.android.analytics.AnalyticsTracker;
import com.jdroid.android.debug.PreferencesAppender;

import java.util.List;

import io.fabric.sdk.android.Kit;

public interface AppModule {

	public void onCreate();

	public void onConfigurationChanged(Configuration newConfig);

	public void onLowMemory();

	public void onTrimMemory(int level);

	public void attachBaseContext(Context base);

	public void onInstanceIdTokenRefresh();

	public void onGooglePlayServicesUpdated();

	public List<? extends AnalyticsTracker> getAnalyticsTrackers();

	public List<Kit> getFabricKits();

	public List<PreferencesAppender> getPreferencesAppenders();
}
