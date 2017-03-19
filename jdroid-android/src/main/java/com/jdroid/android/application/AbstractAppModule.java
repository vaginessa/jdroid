package com.jdroid.android.application;

import android.content.Context;
import android.content.res.Configuration;
import android.support.annotation.CallSuper;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.jdroid.android.activity.AbstractFragmentActivity;
import com.jdroid.android.activity.ActivityDelegate;
import com.jdroid.android.analytics.CoreAnalyticsTracker;
import com.jdroid.android.debug.PreferencesAppender;
import com.jdroid.android.firebase.remoteconfig.RemoteConfigParameter;
import com.jdroid.android.fragment.FragmentDelegate;
import com.jdroid.java.analytics.AnalyticsSender;
import com.jdroid.java.analytics.AnalyticsTracker;
import com.jdroid.java.collections.Lists;

import java.util.List;
import java.util.Map;

public abstract class AbstractAppModule implements AppModule {

	private AnalyticsSender<? extends AnalyticsTracker> analyticsSender;

	@CallSuper
	@MainThread
	@Override
	public void onCreate() {
		analyticsSender = createModuleAnalyticsSender(createModuleAnalyticsTrackers());
	}

	@NonNull
	@Override
	public AnalyticsSender<? extends AnalyticsTracker> createModuleAnalyticsSender(List<? extends AnalyticsTracker> analyticsTrackers) {
		return new AnalyticsSender<>(analyticsTrackers);
	}

	@Override
	public List<? extends AnalyticsTracker> createModuleAnalyticsTrackers() {
		return Lists.newArrayList();
	}

	@Override
	public AnalyticsSender<? extends AnalyticsTracker> getAnalyticsSender() {
		return analyticsSender;
	}

	@MainThread
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// Do Nothing
	}

	@MainThread
	@Override
	public void onLowMemory() {
		// Do Nothing
	}

	@MainThread
	@Override
	public void onTrimMemory(int level) {
		// Do Nothing
	}

	@MainThread
	@Override
	public void attachBaseContext(Context base) {
		// Do Nothing
	}

	@Override
	public void onInstanceIdTokenRefresh() {
		// Do Nothing
	}

	@MainThread
	@Override
	public void onGooglePlayServicesUpdated() {
		// Do Nothing
	}

	@MainThread
	@Override
	public void onLocaleChanged() {
		// Do Nothing
	}

	@MainThread
	@Override
	public void onInitExceptionHandler(Map<String, String> metadata) {
		// Do Nothing
	}

	@Override
	public List<? extends CoreAnalyticsTracker> createCoreAnalyticsTrackers() {
		return Lists.newArrayList();
	}

	public List<PreferencesAppender> getPreferencesAppenders() {
		return Lists.newArrayList();
	}

	@Override
	public void onInitializeGcmTasks() {
		// Do Nothing
	}

	@MainThread
	@Override
	public ActivityDelegate createActivityDelegate(AbstractFragmentActivity abstractFragmentActivity) {
		return null;
	}

	@MainThread
	@Override
	public FragmentDelegate createFragmentDelegate(Fragment fragment) {
		return null;
	}

	@MainThread
	@Override
	public List<RemoteConfigParameter> createRemoteConfigParameters() {
		return null;
	}
}
