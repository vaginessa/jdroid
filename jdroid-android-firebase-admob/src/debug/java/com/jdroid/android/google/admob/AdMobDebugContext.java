package com.jdroid.android.google.admob;

import com.jdroid.android.debug.PreferencesAppender;
import com.jdroid.android.utils.SharedPreferencesHelper;
import com.jdroid.java.collections.Lists;
import com.jdroid.java.concurrent.ExecutorUtils;

import java.util.List;

import static com.jdroid.android.google.admob.AdMobAppContext.TEST_AD_UNIT_ID_ENABLED;

public class AdMobDebugContext {

	public AdMobDebugContext() {
		ExecutorUtils.execute(new Runnable() {
			@Override
			public void run() {
				// This is required to initialize the prefs to display on the debug settings
				if (!SharedPreferencesHelper.get().hasPreference(AdMobRemoteConfigParameter.ADS_ENABLED.getKey())) {
					SharedPreferencesHelper.get().savePreferenceAsync(AdMobRemoteConfigParameter.ADS_ENABLED.getKey(), AdMobAppModule.get().getAdMobAppContext().areAdsEnabledByDefault());
				}
				if (!SharedPreferencesHelper.get().hasPreference(TEST_AD_UNIT_ID_ENABLED)) {
					SharedPreferencesHelper.get().savePreferenceAsync(TEST_AD_UNIT_ID_ENABLED, true);
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
