package com.jdroid.android.debug;

import android.app.Activity;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceCategory;
import android.preference.PreferenceGroup;

import com.jdroid.android.R;
import com.jdroid.android.context.UsageStats;

public class UsageStatsDebugPrefsAppender implements PreferencesAppender {

	@Override
	public void initPreferences(final Activity activity, PreferenceGroup preferenceGroup) {

		PreferenceCategory preferenceCategory = new PreferenceCategory(activity);
		preferenceCategory.setTitle(R.string.usageStats);
		preferenceGroup.addPreference(preferenceCategory);

		Preference preference = new Preference(activity);
		preference.setTitle(R.string.reset);
		preference.setSummary(R.string.reset);
		preference.setOnPreferenceClickListener(new OnPreferenceClickListener() {

			@Override
			public boolean onPreferenceClick(Preference preference) {
				UsageStats.reset();
				return true;
			}
		});
		preferenceCategory.addPreference(preference);

		preference = new Preference(activity);
		preference.setTitle(R.string.simulateHeavyUsage);
		preference.setSummary(R.string.simulateHeavyUsage);
		preference.setOnPreferenceClickListener(new OnPreferenceClickListener() {

			@Override
			public boolean onPreferenceClick(Preference preference) {
				UsageStats.simulateHeavyUsage();
				return true;
			}
		});
		preferenceCategory.addPreference(preference);
	}

	@Override
	public Boolean isEnabled() {
		return true;
	}
}
