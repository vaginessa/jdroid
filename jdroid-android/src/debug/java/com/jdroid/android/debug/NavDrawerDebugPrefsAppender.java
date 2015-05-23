package com.jdroid.android.debug;

import android.app.Activity;
import android.preference.CheckBoxPreference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceScreen;

import com.jdroid.android.R;
import com.jdroid.android.context.AppContext;
import com.jdroid.android.navdrawer.NavDrawer;

public class NavDrawerDebugPrefsAppender implements PreferencesAppender {
	
	/**
	 * @see com.jdroid.android.debug.PreferencesAppender#initPreferences(android.app.Activity,
	 *      android.preference.PreferenceScreen)
	 */
	@Override
	public void initPreferences(Activity activity, PreferenceScreen preferenceScreen) {
		
		PreferenceCategory preferenceCategory = new PreferenceCategory(activity);
		preferenceCategory.setTitle(R.string.navDrawerSettings);
		preferenceScreen.addPreference(preferenceCategory);
		
		CheckBoxPreference checkBoxPreference = new CheckBoxPreference(activity);
		checkBoxPreference.setKey(NavDrawer.NAV_DRAWER_MANUALLY_USED);
		checkBoxPreference.setTitle(R.string.navDrawerManuallyUsedTitle);
		checkBoxPreference.setSummary(R.string.navDrawerManuallyUsedDescription);
		preferenceCategory.addPreference(checkBoxPreference);
		
		checkBoxPreference = new CheckBoxPreference(activity);
		checkBoxPreference.setKey(AppContext.USER_DATA_MOCKED);
		checkBoxPreference.setTitle(R.string.userDataMockedTitle);
		checkBoxPreference.setSummary(R.string.userDataMockedDescription);
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
