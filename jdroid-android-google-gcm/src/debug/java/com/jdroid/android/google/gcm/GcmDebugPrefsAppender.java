package com.jdroid.android.google.gcm;

import android.app.Activity;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceGroup;

import com.google.firebase.messaging.RemoteMessage;
import com.jdroid.android.debug.PreferencesAppender;
import com.jdroid.java.collections.Lists;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class GcmDebugPrefsAppender extends PreferencesAppender {
	
	private Map<GcmMessage, Map<String, String>> gcmMessagesMap;
	
	public GcmDebugPrefsAppender(Map<GcmMessage, Map<String, String>> gcmMessagesMap) {
		this.gcmMessagesMap = gcmMessagesMap;
	}

	@Override
	public int getNameResId() {
		return R.string.jdroid_gcmSettings;
	}
	
	@Override
	public void initPreferences(Activity activity, PreferenceGroup preferenceGroup) {
		ListPreference preference = new ListPreference(activity);
		preference.setTitle(R.string.jdroid_emulateGcmMessageTitle);
		preference.setDialogTitle(R.string.jdroid_emulateGcmMessageTitle);
		preference.setSummary(R.string.jdroid_emulateGcmMessageDescription);
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
				
				for (Entry<GcmMessage, Map<String, String>> entry : gcmMessagesMap.entrySet()) {
					if (entry.getKey().getMessageKey().equals(newValue.toString())) {
						RemoteMessage.Builder builder = new RemoteMessage.Builder("to");
						if (entry.getValue() != null) {
							for(Map.Entry<String, String> each : entry.getValue().entrySet()) {
								builder.addData(each.getKey(), each.getValue());
							}
						}
						entry.getKey().handle(builder.build());
						break;
					}
				}
				return false;
			}
		});
		preferenceGroup.addPreference(preference);

		Preference registerDevicePreference = new Preference(activity);
		registerDevicePreference.setTitle(R.string.jdroid_registerDeviceTitle);
		registerDevicePreference.setSummary(R.string.jdroid_registerDeviceTitle);
		registerDevicePreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {

			@Override
			public boolean onPreferenceClick(Preference preference) {
				new GcmRegistrationCommand().start(true);
				return true;
			}
		});
		preferenceGroup.addPreference(registerDevicePreference);
	}
	
	@Override
	public Boolean isEnabled() {
		return (gcmMessagesMap != null) && !gcmMessagesMap.isEmpty();
	}
}
