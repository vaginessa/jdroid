package com.jdroid.android.debug;

import android.app.Activity;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceCategory;
import android.preference.PreferenceScreen;

import com.jdroid.android.R;
import com.jdroid.java.collections.Lists;
import com.jdroid.java.collections.Maps;
import com.jdroid.java.http.Server;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class ServersDebugPrefsAppender implements PreferencesAppender {
	
	private Map<Class<? extends Server>, List<? extends Server>> serversMap = Maps.newHashMap();
	
	public ServersDebugPrefsAppender(Map<Class<? extends Server>, List<? extends Server>> serversMap) {
		this.serversMap = serversMap;
	}
	
	/**
	 * @see com.jdroid.android.debug.PreferencesAppender#initPreferences(android.app.Activity,
	 *      android.preference.PreferenceScreen)
	 */
	@Override
	public void initPreferences(Activity activity, PreferenceScreen preferenceScreen) {
		
		PreferenceCategory preferenceCategory = new PreferenceCategory(activity);
		preferenceCategory.setTitle(R.string.serversSettings);
		preferenceScreen.addPreference(preferenceCategory);
		
		for (final Entry<Class<? extends Server>, List<? extends Server>> entry : serversMap.entrySet()) {
			ListPreference preference = new ListPreference(activity);
			preference.setKey(entry.getKey().getSimpleName());
			preference.setTitle(entry.getKey().getSimpleName());
			preference.setDialogTitle(entry.getKey().getSimpleName());
			preference.setSummary(entry.getKey().getSimpleName());
			
			List<CharSequence> entries = Lists.newArrayList();
			for (Server each : entry.getValue()) {
				entries.add(each.getName());
			}
			preference.setEntries(entries.toArray(new CharSequence[0]));
			preference.setEntryValues(entries.toArray(new CharSequence[0]));
			preference.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {

				@Override
				public boolean onPreferenceChange(Preference preference, Object newValue) {
					for (Server each : entry.getValue()) {
						if (each.getName().equals(newValue)) {
							onServerPreferenceChange(each);
							break;
						}
					}
					return true;
				}
			});
			preferenceCategory.addPreference(preference);
		}
		
	}

	protected void onServerPreferenceChange(Server each) {
		// Do nothing
	}
	
	/**
	 * @see com.jdroid.android.debug.PreferencesAppender#isEnabled()
	 */
	@Override
	public Boolean isEnabled() {
		return (serversMap != null) && !serversMap.isEmpty();
	}
}
