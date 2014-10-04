package com.jdroid.android.debug;

import android.app.Activity;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceCategory;
import android.preference.PreferenceScreen;
import com.jdroid.android.AbstractApplication;
import com.jdroid.android.R;

public class DebugHttpCacheHelper implements PreferencesAppender {
	
	/**
	 * @see com.jdroid.android.debug.PreferencesAppender#initPreferences(android.app.Activity,
	 *      android.preference.PreferenceScreen)
	 */
	@Override
	public void initPreferences(Activity activity, PreferenceScreen preferenceScreen) {
		
		PreferenceCategory preferenceCategory = new PreferenceCategory(activity);
		preferenceCategory.setTitle(R.string.httpCache);
		preferenceScreen.addPreference(preferenceCategory);
		
		Preference preference = new Preference(activity);
		preference.setTitle(R.string.clearHttpCache);
		preference.setSummary(R.string.clearHttpCache);
		preference.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			
			@Override
			public boolean onPreferenceClick(Preference preference) {
				AbstractApplication.get().cleanFileSystemCache();
				return true;
			}
		});
		preferenceCategory.addPreference(preference);
	}
}
