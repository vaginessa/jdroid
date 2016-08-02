package com.jdroid.android.about;

import android.app.Activity;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceGroup;

import com.jdroid.android.about.appinvite.AppInviteStats;
import com.jdroid.android.debug.PreferencesAppender;

public class AppInviteDebugPrefsAppender extends PreferencesAppender {

	@Override
	public int getNameResId() {
		return R.string.debugAppInvite;
	}

	@Override
	public void initPreferences(final Activity activity, PreferenceGroup preferenceGroup) {
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
		preferenceGroup.addPreference(preference);
	}
}
