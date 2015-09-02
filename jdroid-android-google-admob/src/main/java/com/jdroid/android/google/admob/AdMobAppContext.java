package com.jdroid.android.google.admob;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.jdroid.android.application.AbstractApplication;
import com.jdroid.java.collections.Sets;
import com.jdroid.java.utils.StringUtils;

import java.util.Set;

public class AdMobAppContext {

	public static final String ADS_ENABLED = "adsEnabled";

	private Boolean adsEnabled;

	public AdMobAppContext() {
		adsEnabled = areAdsEnabledByDefault();
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(AbstractApplication.get());
		if (!sharedPreferences.contains(ADS_ENABLED)) {
			SharedPreferences.Editor editor = sharedPreferences.edit();
			editor.putBoolean(ADS_ENABLED, adsEnabled);
			editor.apply();
		}
	}

	public Boolean areAdsEnabledByDefault() {
		return AbstractApplication.get().getBuildConfigValue("ADS_ENABLED", false);
	}

	/**
	 * @return Whether the application has ads enabled or not
	 */
	public Boolean areAdsEnabled() {
		return PreferenceManager.getDefaultSharedPreferences(AbstractApplication.get()).getBoolean(ADS_ENABLED,
				adsEnabled);
	}

	/**
	 * @return The MD5-hashed ID of the devices that should display mocked ads
	 */
	public Set<String> getTestDevicesIds() {
		String testDevicesIds = AbstractApplication.get().getBuildConfigValue("ADS_TEST_DEVICES_IDS", null);
		return testDevicesIds != null ? Sets.newHashSet(StringUtils.splitToCollectionWithCommaSeparator(testDevicesIds)) : Sets.<String>newHashSet();
	}

	/**
	 * @return The AdMob Publisher ID
	 */
	public String getDefaultAdUnitId() {
		return AbstractApplication.get().getBuildConfigValue("AD_UNIT_ID", null);
	}
}
