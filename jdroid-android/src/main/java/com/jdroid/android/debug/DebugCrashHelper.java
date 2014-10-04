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
import com.jdroid.java.exception.ApplicationException;
import com.jdroid.java.exception.BusinessException;
import com.jdroid.java.exception.ConnectionException;

public class DebugCrashHelper {
	
	private static final String UI_THREAD_KEY = "uiThread";
	private static final String CRASH_TYPE_KEY = "crashType";
	
	public static void initPreferences(final Activity activity, PreferenceScreen preferenceScreen) {
		
		PreferenceCategory preferenceCategory = new PreferenceCategory(activity);
		preferenceCategory.setTitle(R.string.exceptionHandling);
		preferenceScreen.addPreference(preferenceCategory);
		
		ListPreference preference = new ListPreference(activity);
		preference.setKey(CRASH_TYPE_KEY);
		preference.setTitle(R.string.exceptionType);
		preference.setDialogTitle(R.string.exceptionType);
		preference.setSummary(R.string.exceptionType);
		List<CharSequence> entries = Lists.newArrayList();
		entries.add(BusinessException.class.getSimpleName());
		entries.add(ConnectionException.class.getSimpleName());
		entries.add(ApplicationException.class.getSimpleName());
		entries.add(RuntimeException.class.getSimpleName());
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
				String crashType = PreferenceManager.getDefaultSharedPreferences(AbstractApplication.get()).getString(
					CRASH_TYPE_KEY, BusinessException.class.getSimpleName());
				CrashGenerator.crash(crashType, !uiThread);
				return true;
			}
		});
		preferenceCategory.addPreference(crashPreference);
	}
}
