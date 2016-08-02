package com.jdroid.android.debug;

import android.app.Activity;
import android.preference.CheckBoxPreference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceGroup;

import com.jdroid.android.R;
import com.jdroid.android.navdrawer.NavDrawer;

public class NavDrawerDebugPrefsAppender extends AbstractPreferencesAppender {
	
	@Override
	public void initPreferences(Activity activity, PreferenceGroup preferenceGroup) {
		
		PreferenceCategory preferenceCategory = new PreferenceCategory(activity);
		preferenceCategory.setTitle(R.string.navDrawerSettings);
		preferenceGroup.addPreference(preferenceCategory);
		
		CheckBoxPreference checkBoxPreference = new CheckBoxPreference(activity);
		checkBoxPreference.setKey(NavDrawer.NAV_DRAWER_MANUALLY_USED);
		checkBoxPreference.setTitle(R.string.navDrawerManuallyUsedTitle);
		checkBoxPreference.setSummary(R.string.navDrawerManuallyUsedDescription);
		preferenceCategory.addPreference(checkBoxPreference);
	}
}
