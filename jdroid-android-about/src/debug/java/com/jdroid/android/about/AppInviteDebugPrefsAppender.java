package com.jdroid.android.about;

import android.app.Activity;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceCategory;
import android.preference.PreferenceGroup;

import com.jdroid.android.about.appinvite.AppInviteStats;
import com.jdroid.android.debug.AbstractPreferencesAppender;

public class AppInviteDebugPrefsAppender extends AbstractPreferencesAppender {

	@Override
	public void initPreferences(final Activity activity, PreferenceGroup preferenceGroup) {

		PreferenceCategory preferenceCategory = new PreferenceCategory(activity);
		preferenceCategory.setTitle(R.string.debugAppInvite);
		preferenceGroup.addPreference(preferenceCategory);

		Preference preference = new Preference(activity);
		preference.setTitle(R.string.reset);
		preference.setSummary(R.string.reset);
		preference.setOnPreferenceClickListener(new OnPreferenceClickListener() {

			@Override
			public boolean onPreferenceClick(Preference preference) {
				AppInviteStats.reset();
				return true;
			}
		});
		preferenceCategory.addPreference(preference);
	}
}
