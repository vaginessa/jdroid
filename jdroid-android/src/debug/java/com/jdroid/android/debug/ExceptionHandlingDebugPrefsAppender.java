package com.jdroid.android.debug;

import java.util.List;
import android.app.Activity;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceCategory;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import com.jdroid.android.AbstractApplication;
import com.jdroid.android.R;
import com.jdroid.java.collections.Lists;

public class ExceptionHandlingDebugPrefsAppender implements PreferencesAppender {
	
	private static final String UI_THREAD_KEY = "uiThread";
	private static final String CRASH_TYPE_KEY = "crashType";
	
	/**
	 * @see com.jdroid.android.debug.PreferencesAppender#initPreferences(android.app.Activity,
	 *      android.preference.PreferenceScreen)
	 */
	@Override
	public void initPreferences(Activity activity, PreferenceScreen preferenceScreen) {
		
		PreferenceCategory preferenceCategory = new PreferenceCategory(activity);
		preferenceCategory.setTitle(R.string.exceptionHandlingSettings);
		preferenceScreen.addPreference(preferenceCategory);
		
		ListPreference preference = new ListPreference(activity);
		preference.setKey(CRASH_TYPE_KEY);
		preference.setTitle(R.string.exceptionType);
		preference.setDialogTitle(R.string.exceptionType);
		preference.setSummary(R.string.exceptionType);
		List<CharSequence> entries = Lists.newArrayList();
		for (ExceptionType each : ExceptionType.values()) {
			entries.add(each.name());
		}
		preference.setEntries(entries.toArray(new CharSequence[0]));
		preference.setEntryValues(entries.toArray(new CharSequence[0]));
		preferenceCategory.addPreference(preference);
		
		CheckBoxPreference checkBoxPreference = new CheckBoxPreference(activity);
		checkBoxPreference.setKey(UI_THREAD_KEY);
		checkBoxPreference.setTitle(R.string.uiThread);
		checkBoxPreference.setSummary(R.string.uiThread);
		preferenceCategory.addPreference(checkBoxPreference);
		
		Preference crashPreference = new Preference(activity);
		crashPreference.setTitle(R.string.crash);
		crashPreference.setSummary(R.string.crash);
		crashPreference.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			
			@Override
			public boolean onPreferenceClick(Preference preference) {
				
				Boolean uiThread = PreferenceManager.getDefaultSharedPreferences(AbstractApplication.get()).getBoolean(
					UI_THREAD_KEY, false);
				ExceptionType exceptionType = ExceptionType.valueOf(PreferenceManager.getDefaultSharedPreferences(
					AbstractApplication.get()).getString(CRASH_TYPE_KEY, ExceptionType.UNEXPECTED_EXCEPTION.name()));
				CrashGenerator.crash(exceptionType, !uiThread);
				return true;
			}
		});
		preferenceCategory.addPreference(crashPreference);
	}
	
	/**
	 * @see com.jdroid.android.debug.PreferencesAppender#isEnabled()
	 */
	@Override
	public Boolean isEnabled() {
		return true;
	}
}
