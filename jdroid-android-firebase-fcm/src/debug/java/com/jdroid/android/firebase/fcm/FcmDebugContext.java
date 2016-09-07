package com.jdroid.android.firebase.fcm;

import com.jdroid.android.debug.PreferencesAppender;
import com.jdroid.java.collections.Lists;
import com.jdroid.java.collections.Maps;

import java.util.List;
import java.util.Map;

public class FcmDebugContext {

	public List<PreferencesAppender> getPreferencesAppenders() {
		List<PreferencesAppender> appenders = Lists.newArrayList();
		appenders.add(createFcmDebugPrefsAppender());
		return appenders;
	}

	protected FcmDebugPrefsAppender createFcmDebugPrefsAppender() {
		return new FcmDebugPrefsAppender(getFcmMessagesMap());
	}

	protected Map<FcmMessage, Map<String, String>> getFcmMessagesMap() {
		return Maps.newHashMap();
	}
	
}
