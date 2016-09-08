package com.jdroid.android.sample.ui.firebase.remoteconfig;

import com.jdroid.android.firebase.remoteconfig.FirebaseRemoteConfigAppContext;
import com.jdroid.java.collections.Maps;

import java.util.Map;

public class AndroidFirebaseRemoteConfigAppContext extends FirebaseRemoteConfigAppContext {

	@Override
	public Map<String, Object> getRemoteConfigDefaults() {
		Map<String, Object> defaults = Maps.newHashMap();
		defaults.put("sampleConfig1", "defaultConfigValue1");
		defaults.put("sampleConfig2", "defaultConfigValue2");
		defaults.put("sampleConfig3", "defaultConfigValue3");
		return defaults;
	}
}
