package com.jdroid.android.debug;

import android.app.Activity;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceCategory;
import android.preference.PreferenceScreen;
import com.jdroid.android.AbstractApplication;
import com.jdroid.android.R;
import com.jdroid.android.log.DatabaseLog;
import com.jdroid.java.concurrent.ExecutorUtils;
import com.jdroid.java.repository.Repository;
import com.jdroid.java.utils.LoggerUtils;

import org.slf4j.Logger;

public class LogsDebugPrefsAppender implements PreferencesAppender {

	private static final Logger LOGGER_UTILS = LoggerUtils.getLogger(LogsDebugPrefsAppender.class);
	
	public static void clean() {
		ExecutorUtils.execute(new Runnable() {

			@Override
			public void run() {

				try {
					Repository<DatabaseLog> repository = AbstractApplication.get().getRepositoryInstance(DatabaseLog.class);
					repository.removeAll();
				} catch (Exception e) {
					AbstractApplication.get().getExceptionHandler().logHandledException(e);
				}
			}
		});
	}

	/**
	 * @see com.jdroid.android.debug.PreferencesAppender#initPreferences(android.app.Activity,
	 *      android.preference.PreferenceScreen)
	 */
	@Override
	public void initPreferences(final Activity activity, PreferenceScreen preferenceScreen) {
		
		PreferenceCategory preferenceCategory = new PreferenceCategory(activity);
		preferenceCategory.setTitle(R.string.debugDatabase);
		preferenceScreen.addPreference(preferenceCategory);
		
		Preference preference = new Preference(activity);
		preference.setTitle(R.string.cleanDebugData);
		preference.setSummary(R.string.cleanDebugData);
		preference.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			
			@Override
			public boolean onPreferenceClick(Preference preference) {
				LogsDebugPrefsAppender.clean();
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
		return AbstractApplication.get().isDebugLogRepositoryEnabled();
	}
}
