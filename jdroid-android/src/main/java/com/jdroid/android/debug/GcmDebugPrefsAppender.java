package com.jdroid.android.debug;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import android.app.Activity;
import android.content.Intent;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceCategory;
import android.preference.PreferenceScreen;
import com.jdroid.android.R;
import com.jdroid.android.gcm.GcmMessage;
import com.jdroid.java.collections.Lists;

public class GcmDebugPrefsAppender implements PreferencesAppender {
	
	private Map<GcmMessage, EmulatedGcmMessageIntentBuilder> gcmMessagesMap;
	
	public GcmDebugPrefsAppender(Map<GcmMessage, EmulatedGcmMessageIntentBuilder> gcmMessagesMap) {
		this.gcmMessagesMap = gcmMessagesMap;
	}
	
	/**
	 * @see com.jdroid.android.debug.PreferencesAppender#initPreferences(android.app.Activity,
	 *      android.preference.PreferenceScreen)
	 */
	@Override
	public void initPreferences(Activity activity, PreferenceScreen preferenceScreen) {
		
		PreferenceCategory preferenceCategory = new PreferenceCategory(activity);
		preferenceCategory.setTitle(R.string.gcmSettings);
		preferenceScreen.addPreference(preferenceCategory);
		
		ListPreference preference = new ListPreference(activity);
		preference.setTitle(R.string.emulateGcmMessageTitle);
		preference.setDialogTitle(R.string.emulateGcmMessageTitle);
		preference.setSummary(R.string.emulateGcmMessageDescription);
		List<CharSequence> entries = Lists.newArrayList();
		for (GcmMessage entry : gcmMessagesMap.keySet()) {
			entries.add(entry.getMessageKey());
		}
		preference.setEntries(entries.toArray(new CharSequence[0]));
		preference.setEntryValues(entries.toArray(new CharSequence[0]));
		preference.setPersistent(false);
		preference.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
			
			@Override
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				
				for (Entry<GcmMessage, EmulatedGcmMessageIntentBuilder> entry : gcmMessagesMap.entrySet()) {
					if (entry.getKey().getMessageKey().equals(newValue.toString())) {
						Intent intent = entry.getValue().buildIntent();
						if (intent != null) {
							entry.getKey().handle(intent);
						}
						break;
					}
				}
				return false;
			}
		});
		preferenceCategory.addPreference(preference);
	}
	
	/**
	 * @see com.jdroid.android.debug.PreferencesAppender#isEnabled()
	 */
	@Override
	public Boolean isEnabled() {
		return (gcmMessagesMap != null) && !gcmMessagesMap.isEmpty();
	}
}
