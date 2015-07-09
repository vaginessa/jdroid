package com.jdroid.android.debug;

import android.app.Activity;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceCategory;
import android.preference.PreferenceGroup;

import com.jdroid.android.ActivityLauncher;
import com.jdroid.android.R;

public class InfoDebugPrefsAppender implements PreferencesAppender {

	/**
	 * @see PreferencesAppender#initPreferences(Activity, PreferenceGroup)
	 */
	@Override
	public void initPreferences(final Activity activity, PreferenceGroup preferenceGroup) {

		PreferenceCategory preferenceCategory = new PreferenceCategory(activity);
		preferenceCategory.setTitle(R.string.debugInfoCategory);
		preferenceGroup.addPreference(preferenceCategory);

		Preference crashPreference = new Preference(activity);
		crashPreference.setTitle(R.string.debugInfo);
		crashPreference.setSummary(R.string.debugInfo);
		crashPreference.setOnPreferenceClickListener(new OnPreferenceClickListener() {

			@Override
			public boolean onPreferenceClick(Preference preference) {
				ActivityLauncher.launchActivity(DebugInfoActivity.class);
				return true;
			}
		});
		preferenceCategory.addPreference(crashPreference);
	}

	/**
	 * @see PreferencesAppender#isEnabled()
	 */
	@Override
	public Boolean isEnabled() {
		return true;
	}
}
