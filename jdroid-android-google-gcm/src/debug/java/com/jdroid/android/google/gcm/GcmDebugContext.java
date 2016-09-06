package com.jdroid.android.google.gcm;

import com.jdroid.android.debug.PreferencesAppender;
import com.jdroid.java.collections.Lists;
import com.jdroid.java.collections.Maps;

import java.util.List;
import java.util.Map;

public class GcmDebugContext {

	public List<PreferencesAppender> getPreferencesAppenders() {
		List<PreferencesAppender> appenders = Lists.newArrayList();
		appenders.add(createGcmDebugPrefsAppender());
		return appenders;
	}

	protected GcmDebugPrefsAppender createGcmDebugPrefsAppender() {
		return new GcmDebugPrefsAppender(getGcmMessagesMap());
	}

	protected Map<GcmMessage, Map<String, String>> getGcmMessagesMap() {
		return Maps.newHashMap();
	}
	
}
