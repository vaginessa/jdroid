package com.jdroid.android.google.admob;

import android.app.Activity;
import android.preference.CheckBoxPreference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceGroup;

import com.jdroid.android.debug.AbstractPreferencesAppender;

public class AdsDebugPrefsAppender extends AbstractPreferencesAppender {
	
	@Override
	public void initPreferences(Activity activity, PreferenceGroup preferenceGroup) {
		
		PreferenceCategory preferenceCategory = new PreferenceCategory(activity);
		preferenceCategory.setTitle(R.string.adsSettings);
		preferenceGroup.addPreference(preferenceCategory);
		
		CheckBoxPreference checkBoxPreference = new CheckBoxPreference(activity);
		checkBoxPreference.setKey(AdMobAppContext.ADS_ENABLED);
		checkBoxPreference.setTitle(R.string.adsEnabledTitle);
		checkBoxPreference.setSummary(R.string.adsEnabledDescription);
		preferenceCategory.addPreference(checkBoxPreference);
	}
}
