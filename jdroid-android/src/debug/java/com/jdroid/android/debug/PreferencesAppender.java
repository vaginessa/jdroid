package com.jdroid.android.debug;

import android.app.Activity;
import android.preference.PreferenceScreen;

public interface PreferencesAppender {
	
	public void initPreferences(Activity activity, PreferenceScreen preferenceScreen);
	
	public Boolean isEnabled();
	
}
