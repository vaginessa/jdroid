package com.jdroid.android.application;

import android.content.Context;
import android.content.res.Configuration;
import android.support.annotation.MainThread;
import android.support.v4.app.Fragment;

import com.jdroid.android.activity.AbstractFragmentActivity;
import com.jdroid.android.activity.ActivityDelegate;
import com.jdroid.android.analytics.CoreAnalyticsTracker;
import com.jdroid.android.debug.PreferencesAppender;
import com.jdroid.android.firebase.remoteconfig.RemoteConfigParameter;
import com.jdroid.android.fragment.FragmentDelegate;
import com.jdroid.java.analytics.AnalyticsSender;
import com.jdroid.java.analytics.AnalyticsTracker;

import java.util.List;
import java.util.Map;

public interface AppModule {

	@MainThread
	public void onCreate();

	@MainThread
	public void onConfigurationChanged(Configuration newConfig);

	@MainThread
	public void onLowMemory();

	@MainThread
	public void onTrimMemory(int level);

	@MainThread
	public void attachBaseContext(Context base);

	public void onInstanceIdTokenRefresh();

	@MainThread
	public void onGooglePlayServicesUpdated();

	@MainThread
	public void onLocaleChanged();

	@MainThread
	public void onInitExceptionHandler(Map<String, String> metadata);

	public List<? extends CoreAnalyticsTracker> createCoreAnalyticsTrackers();

	public List<PreferencesAppender> getPreferencesAppenders();

	public void onInitializeGcmTasks();

	@MainThread
	public ActivityDelegate createActivityDelegate(AbstractFragmentActivity abstractFragmentActivity);

	@MainThread
	public FragmentDelegate createFragmentDelegate(Fragment fragment);

	// Analytics

	public AnalyticsSender<? extends AnalyticsTracker> createModuleAnalyticsSender(List<? extends AnalyticsTracker> analyticsTrackers);

	public List<? extends AnalyticsTracker> createModuleAnalyticsTrackers();

	public AnalyticsSender<? extends AnalyticsTracker> getAnalyticsSender();

	@MainThread
	public List<RemoteConfigParameter> createRemoteConfigParameters();
}
