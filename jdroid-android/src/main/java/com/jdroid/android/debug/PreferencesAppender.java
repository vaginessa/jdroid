package com.jdroid.android.debug;

import android.app.Activity;
import android.preference.PreferenceGroup;

public interface PreferencesAppender {
	
	public void initPreferences(Activity activity, PreferenceGroup preferenceGroup);
	
	public Boolean isEnabled();
	
}
