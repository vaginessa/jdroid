package com.jdroid.android.debug;

import android.app.Activity;
import android.preference.CheckBoxPreference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceScreen;
import com.jdroid.android.R;
import com.jdroid.android.context.AppContext;

public class AdsDebugPrefsAppender implements PreferencesAppender {
	
	/**
	 * @see com.jdroid.android.debug.PreferencesAppender#initPreferences(android.app.Activity,
	 *      android.preference.PreferenceScreen)
	 */
	@Override
	public void initPreferences(Activity activity, PreferenceScreen preferenceScreen) {
		
		PreferenceCategory preferenceCategory = new PreferenceCategory(activity);
		preferenceCategory.setTitle(R.string.adsSettings);
		preferenceScreen.addPreference(preferenceCategory);
		
		CheckBoxPreference checkBoxPreference = new CheckBoxPreference(activity);
		checkBoxPreference.setKey(AppContext.ADS_ENABLED);
		checkBoxPreference.setTitle(R.string.adsEnabledTitle);
		checkBoxPreference.setSummary(R.string.adsEnabledDescription);
		preferenceCategory.addPreference(checkBoxPreference);
	}
	
	/**
	 * @see com.jdroid.android.debug.PreferencesAppender#isEnabled()
	 */
	@Override
	public Boolean isEnabled() {
		return true;
	}
}
