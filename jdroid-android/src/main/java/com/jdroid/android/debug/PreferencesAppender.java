package com.jdroid.android.debug;

import android.app.Activity;
import android.preference.PreferenceGroup;

import java.util.List;

public interface PreferencesAppender {
	
	public void initPreferences(Activity activity, PreferenceGroup preferenceGroup);
	
	public Boolean isEnabled();

	public List<String> getRequiredPermissions();

}
