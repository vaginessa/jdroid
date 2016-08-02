package com.jdroid.android.debug;

import android.app.Activity;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceCategory;
import android.preference.PreferenceGroup;

import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.R;

public class ImageLoaderDebugPrefsAppender extends AbstractPreferencesAppender {
	
	@Override
	public void initPreferences(Activity activity, PreferenceGroup preferenceGroup) {
		
		PreferenceCategory preferenceCategory = new PreferenceCategory(activity);
		preferenceCategory.setTitle(R.string.imageLoaderSettings);
		preferenceGroup.addPreference(preferenceCategory);
		
		Preference preference = new Preference(activity);
		preference.setTitle(R.string.clearImagesDiskCache);
		preference.setSummary(R.string.clearImagesDiskCache);
		preference.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			
			@Override
			public boolean onPreferenceClick(Preference preference) {
				AbstractApplication.get().getImageLoaderHelper().clearDiskCache();
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
				AbstractApplication.get().getImageLoaderHelper().clearMemoryCache();
				return true;
			}
		});
		preferenceCategory.addPreference(preference);
	}
	
	@Override
	public Boolean isEnabled() {
		return AbstractApplication.get().getImageLoaderHelper() != null;
	}
	
}
