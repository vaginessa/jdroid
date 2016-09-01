package com.jdroid.android.debug;

import android.app.Activity;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceGroup;

import com.jdroid.android.R;
import com.jdroid.android.application.AbstractApplication;

public class ImageLoaderDebugPrefsAppender extends PreferencesAppender {

	@Override
	public int getNameResId() {
		return R.string.jdroid_imageLoaderSettings;
	}
	
	@Override
	public void initPreferences(Activity activity, PreferenceGroup preferenceGroup) {
		Preference preference = new Preference(activity);
		preference.setTitle(R.string.jdroid_clearImagesDiskCache);
		preference.setSummary(R.string.jdroid_clearImagesDiskCache);
		preference.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			
			@Override
			public boolean onPreferenceClick(Preference preference) {
				AbstractApplication.get().getImageLoaderHelper().clearDiskCache();
				return true;
			}
		});
		preferenceGroup.addPreference(preference);
		
		preference = new Preference(activity);
		preference.setTitle(R.string.jdroid_clearImagesMemoryCache);
		preference.setSummary(R.string.jdroid_clearImagesMemoryCache);
		preference.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			
			@Override
			public boolean onPreferenceClick(Preference preference) {
				AbstractApplication.get().getImageLoaderHelper().clearMemoryCache();
				return true;
			}
		});
		preferenceGroup.addPreference(preference);
	}
	
	@Override
	public Boolean isEnabled() {
		return AbstractApplication.get().getImageLoaderHelper() != null;
	}
	
}
