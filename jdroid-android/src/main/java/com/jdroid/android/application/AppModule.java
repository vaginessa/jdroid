package com.jdroid.android.application;

import android.content.Context;
import android.content.res.Configuration;
import android.support.v4.app.Fragment;

import com.jdroid.android.activity.AbstractFragmentActivity;
import com.jdroid.android.activity.ActivityDelegate;
import com.jdroid.android.analytics.AnalyticsTracker;
import com.jdroid.android.debug.PreferencesAppender;
import com.jdroid.android.firebase.remoteconfig.RemoteConfigParameter;
import com.jdroid.android.fragment.FragmentDelegate;
import com.jdroid.java.analytics.BaseAnalyticsSender;
import com.jdroid.java.analytics.BaseAnalyticsTracker;

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

	public void onInitializeGcmTasks();

	public ActivityDelegate createActivityDelegate(AbstractFragmentActivity abstractFragmentActivity);

	public FragmentDelegate createFragmentDelegate(Fragment fragment);

	// Analytics

	public BaseAnalyticsSender<? extends BaseAnalyticsTracker> createModuleAnalyticsSender(List<? extends BaseAnalyticsTracker> analyticsTrackers);

	public List<? extends BaseAnalyticsTracker> createModuleAnalyticsTrackers();

	public BaseAnalyticsSender<? extends BaseAnalyticsTracker> getAnalyticsSender();

	public List<RemoteConfigParameter> getRemoteConfigParameters();
}
