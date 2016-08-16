package com.jdroid.android.debug;

import android.app.Activity;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceGroup;
import android.preference.PreferenceManager;

import com.jdroid.android.R;
import com.jdroid.android.application.AbstractApplication;
import com.jdroid.java.collections.Lists;
import com.jdroid.java.utils.IdGenerator;

import java.util.List;

public class ExceptionHandlingDebugPrefsAppender extends PreferencesAppender {
	
	private static final String UI_THREAD_KEY = "uiThread";
	private static final String CRASH_TYPE_KEY = "crashType";

	@Override
	public int getNameResId() {
		return R.string.exceptionHandlingSettings;
	}
	
	@Override
	public void initPreferences(Activity activity, PreferenceGroup preferenceGroup) {
		
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
		preferenceGroup.addPreference(preference);
		
		CheckBoxPreference checkBoxPreference = new CheckBoxPreference(activity);
		checkBoxPreference.setKey(UI_THREAD_KEY);
		checkBoxPreference.setTitle(R.string.uiThread);
		checkBoxPreference.setSummary(R.string.uiThread);
		preferenceGroup.addPreference(checkBoxPreference);
		
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
		preferenceGroup.addPreference(crashPreference);

		Preference breadcrumbPreference = new Preference(activity);
		breadcrumbPreference.setTitle(R.string.trackErrorBreadcrumb);
		breadcrumbPreference.setSummary(R.string.trackErrorBreadcrumb);
		breadcrumbPreference.setOnPreferenceClickListener(new OnPreferenceClickListener() {

			@Override
			public boolean onPreferenceClick(Preference preference) {
				AbstractApplication.get().getAnalyticsSender().trackErrorBreadcrumb("Sample message " + IdGenerator.getIntId());
				return true;
			}
		});
		preferenceGroup.addPreference(breadcrumbPreference);
	}
}
