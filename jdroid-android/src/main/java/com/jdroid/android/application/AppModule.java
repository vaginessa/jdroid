package com.jdroid.android.application;

import android.content.Context;
import android.content.res.Configuration;
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

import io.fabric.sdk.android.Kit;

public interface AppModule {

	public void onCreate();

	public void onConfigurationChanged(Configuration newConfig);

	public void onLowMemory();

	public void onTrimMemory(int level);

	public void attachBaseContext(Context base);

	public void onInstanceIdTokenRefresh();

	public void onGooglePlayServicesUpdated();

	public void onLocaleChanged();

	public void onInitExceptionHandler(Map<String, String> metadata);

	public List<? extends CoreAnalyticsTracker> createCoreAnalyticsTrackers();

	public List<Kit> getFabricKits();

	public List<PreferencesAppender> getPreferencesAppenders();

	public void onInitializeGcmTasks();

	public ActivityDelegate createActivityDelegate(AbstractFragmentActivity abstractFragmentActivity);

	public FragmentDelegate createFragmentDelegate(Fragment fragment);

	// Analytics

	public AnalyticsSender<? extends AnalyticsTracker> createModuleAnalyticsSender(List<? extends AnalyticsTracker> analyticsTrackers);

	public List<? extends AnalyticsTracker> createModuleAnalyticsTrackers();

	public AnalyticsSender<? extends AnalyticsTracker> getAnalyticsSender();

	public List<RemoteConfigParameter> getRemoteConfigParameters();
}
