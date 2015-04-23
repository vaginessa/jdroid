package com.jdroid.android.debug;

import android.app.Activity;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceCategory;
import android.preference.PreferenceScreen;

import com.jdroid.android.AbstractApplication;
import com.jdroid.android.R;
import com.jdroid.android.utils.ImageLoaderUtils;

public class ImageLoaderDebugPrefsAppender implements PreferencesAppender {
	
	/**
	 * @see com.jdroid.android.debug.PreferencesAppender#initPreferences(android.app.Activity,
	 *      android.preference.PreferenceScreen)
	 */
	@Override
	public void initPreferences(Activity activity, PreferenceScreen preferenceScreen) {
		
		PreferenceCategory preferenceCategory = new PreferenceCategory(activity);
		preferenceCategory.setTitle(R.string.imageLoaderSettings);
		preferenceScreen.addPreference(preferenceCategory);
		
		Preference preference = new Preference(activity);
		preference.setTitle(R.string.clearImagesDiskCache);
		preference.setSummary(R.string.clearImagesDiskCache);
		preference.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			
			@Override
			public boolean onPreferenceClick(Preference preference) {
				ImageLoaderUtils.clearDiskCache();
				return true;
			}
		});
		preferenceCategory.addPreference(preference);
		
		preference = new Preference(activity);
		preference.setTitle(R.string.clearImagesMemoryCache);
		preference.setSummary(R.string.clearImagesMemoryCache);
		preference.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			
			@Override
			public boolean onPreferenceClick(Preference preference) {
				ImageLoaderUtils.clearMemoryCache();
				return true;
			}
		});
		preferenceCategory.addPreference(preference);
	}
	
	/**
	 * @see com.jdroid.android.debug.PreferencesAppender#isEnabled()
	 */
	@Override
	public Boolean isEnabled() {
		return AbstractApplication.get().isImageLoaderEnabled();
	}
	
}
