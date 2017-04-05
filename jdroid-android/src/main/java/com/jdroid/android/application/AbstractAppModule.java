package com.jdroid.android.application;

import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.WorkerThread;
import android.support.v4.app.Fragment;

import com.jdroid.android.activity.AbstractFragmentActivity;
import com.jdroid.android.activity.ActivityDelegate;
import com.jdroid.android.analytics.CoreAnalyticsTracker;
import com.jdroid.android.debug.PreferencesAppender;
import com.jdroid.android.fragment.FragmentDelegate;
import com.jdroid.java.analytics.AnalyticsSender;
import com.jdroid.java.analytics.AnalyticsTracker;
import com.jdroid.java.collections.Lists;

import java.util.List;

public abstract class AbstractAppModule implements AppModule {

	private AnalyticsSender<? extends AnalyticsTracker> analyticsSender;

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
	public synchronized AnalyticsSender<? extends AnalyticsTracker> getAnalyticsSender() {
		if (analyticsSender == null) {
			analyticsSender = createModuleAnalyticsSender(createModuleAnalyticsTrackers());
		}
		return analyticsSender;
	}
	
	@WorkerThread
	@Override
	public void onInstanceIdTokenRefresh() {
		// Do Nothing
	}

	@MainThread
	@Override
	public void onGooglePlayServicesUpdated() {
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
}
