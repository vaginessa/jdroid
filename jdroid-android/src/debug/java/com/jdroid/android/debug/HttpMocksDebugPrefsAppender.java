package com.jdroid.android.debug;

import android.app.Activity;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceGroup;

import com.jdroid.android.R;
import com.jdroid.android.debug.mocks.AndroidJsonMockHttpService;
import com.jdroid.java.collections.Lists;

import java.util.List;

public class HttpMocksDebugPrefsAppender extends AbstractPreferencesAppender {
	
	@Override
	public void initPreferences(Activity activity, PreferenceGroup preferenceGroup) {
		
		PreferenceCategory preferenceCategory = new PreferenceCategory(activity);
		preferenceCategory.setTitle(R.string.httpMocksSettings);
		preferenceGroup.addPreference(preferenceCategory);
		
		CheckBoxPreference checkBoxPreference = new CheckBoxPreference(activity);
		checkBoxPreference.setKey(DebugContext.HTTP_MOCK_ENABLED);
		checkBoxPreference.setTitle(R.string.httpMockEnabledTitle);
		checkBoxPreference.setSummary(R.string.httpMockEnabledDescription);
		preferenceCategory.addPreference(checkBoxPreference);
		
		checkBoxPreference = new CheckBoxPreference(activity);
		checkBoxPreference.setKey(DebugContext.HTTP_MOCK_SLEEP);
		checkBoxPreference.setTitle(R.string.httpMockSleepTitle);
		checkBoxPreference.setSummary(R.string.httpMockSleepDescription);
		// FIXME this is not working
		// checkBoxPreference.setDependency(AppContext.HTTP_MOCK_ENABLED);
		preferenceCategory.addPreference(checkBoxPreference);
		
		ListPreference preference = new ListPreference(activity);
		preference.setKey(AndroidJsonMockHttpService.HTTP_MOCK_CRASH_TYPE);
		preference.setTitle(R.string.httpMockCrashType);
		preference.setDialogTitle(R.string.httpMockCrashType);
		preference.setSummary(R.string.httpMockCrashTypeDescription);
		List<CharSequence> entries = Lists.newArrayList();
		entries.add("None");
		for (ExceptionType each : ExceptionType.values()) {
			entries.add(each.name());
		}
		preference.setEntries(entries.toArray(new CharSequence[0]));
		preference.setEntryValues(entries.toArray(new CharSequence[0]));
		// FIXME this is not working
		// preference.setDependency(AppContext.HTTP_MOCK_ENABLED);
		preferenceCategory.addPreference(preference);

		onInitPreferenceCategory(activity, preferenceCategory);
	}

	protected void onInitPreferenceCategory(Activity activity, PreferenceCategory preferenceCategory) {
		// Do nothing
	}
}
