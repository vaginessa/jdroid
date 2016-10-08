package com.jdroid.android.google.admob;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.debug.PreferencesAppender;
import com.jdroid.java.collections.Lists;
import com.jdroid.java.concurrent.ExecutorUtils;

import java.util.List;

import static com.jdroid.android.google.admob.AdMobAppContext.ADS_ENABLED;
import static com.jdroid.android.google.admob.AdMobAppContext.TEST_AD_UNIT_ID_ENABLED;

public class AdMobDebugContext {

	public AdMobDebugContext() {
		ExecutorUtils.execute(new Runnable() {
			@Override
			public void run() {
				// This is required to initialize the prefs to display on the debug settings
				SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(AbstractApplication.get());
				if (!sharedPreferences.contains(ADS_ENABLED)) {
					SharedPreferences.Editor editor = sharedPreferences.edit();
					editor.putBoolean(ADS_ENABLED, AdMobAppModule.get().getAdMobAppContext().areAdsEnabledByDefault());
					editor.apply();
				}
				if (!sharedPreferences.contains(TEST_AD_UNIT_ID_ENABLED)) {
					SharedPreferences.Editor editor = sharedPreferences.edit();
					editor.putBoolean(TEST_AD_UNIT_ID_ENABLED, true);
					editor.apply();
				}
			}
		});
	}

	public List<PreferencesAppender> getPreferencesAppenders() {
		List<PreferencesAppender> appenders = Lists.newArrayList();
		appenders.add(new AdsDebugPrefsAppender());
		return appenders;
	}
}
