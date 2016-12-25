package com.jdroid.android.application;

import android.content.Context;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.jdroid.android.activity.AbstractFragmentActivity;
import com.jdroid.android.activity.ActivityDelegate;
import com.jdroid.android.analytics.AnalyticsTracker;
import com.jdroid.android.debug.PreferencesAppender;
import com.jdroid.android.firebase.remoteconfig.RemoteConfigParameter;
import com.jdroid.android.fragment.FragmentDelegate;
import com.jdroid.java.analytics.BaseAnalyticsSender;
import com.jdroid.java.analytics.BaseAnalyticsTracker;
import com.jdroid.java.collections.Lists;

import java.util.List;

import io.fabric.sdk.android.Kit;

public abstract class AbstractAppModule implements AppModule {

	private BaseAnalyticsSender<? extends BaseAnalyticsTracker> analyticsSender;

	@Override
	public void onCreate() {
		analyticsSender = createModuleAnalyticsSender(createModuleAnalyticsTrackers());
	}

	@NonNull
	@Override
	public BaseAnalyticsSender<? extends BaseAnalyticsTracker> createModuleAnalyticsSender(List<? extends BaseAnalyticsTracker> analyticsTrackers) {
		return new BaseAnalyticsSender<>(analyticsTrackers);
	}

	@Override
	public List<? extends BaseAnalyticsTracker> createModuleAnalyticsTrackers() {
		return Lists.newArrayList();
	}

	@Override
	public BaseAnalyticsSender<? extends BaseAnalyticsTracker> getAnalyticsSender() {
		return analyticsSender;
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
	public void onGooglePlayServicesUpdated() {
		// Do Nothing
	}

	@Override
	public void onLocaleChanged() {
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

	@Override
	public void onInitializeGcmTasks() {
		// Do Nothing
	}

	@Override
	public ActivityDelegate createActivityDelegate(AbstractFragmentActivity abstractFragmentActivity) {
		return null;
	}

	@Override
	public FragmentDelegate createFragmentDelegate(Fragment fragment) {
		return null;
	}

	@Override
	public List<RemoteConfigParameter> getRemoteConfigParameters() {
		return null;
	}
}
