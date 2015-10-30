package com.jdroid.android.application;

import android.content.Context;
import android.content.res.Configuration;

import com.jdroid.android.analytics.AnalyticsTracker;
import com.jdroid.android.debug.PreferencesAppender;
import com.jdroid.java.collections.Lists;

import java.util.List;

import io.fabric.sdk.android.Kit;

public abstract class AbstractAppModule implements AppModule {

	@Override
	public void onCreate() {
		// Do Nothing
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// Do Nothing
	}

	@Override
	public void onLowMemory() {
		// Do Nothing
	}

	@Override
	public void onTrimMemory(int level) {
		// Do Nothing
	}

	@Override
	public void attachBaseContext(Context base) {
		// Do Nothing
	}

	@Override
	public void onInstanceIdTokenRefresh() {
		// Do Nothing
	}

	@Override
	public List<? extends AnalyticsTracker> getAnalyticsTrackers() {
		return Lists.newArrayList();
	}

	@Override
	public List<Kit> getFabricKits() {
		return Lists.newArrayList();
	}

	public List<PreferencesAppender> getPreferencesAppenders() {
		return Lists.newArrayList();
	}
}
