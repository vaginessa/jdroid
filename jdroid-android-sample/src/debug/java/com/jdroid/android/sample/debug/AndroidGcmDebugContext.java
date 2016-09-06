package com.jdroid.android.sample.debug;

import com.jdroid.android.google.gcm.GcmDebugContext;
import com.jdroid.android.google.gcm.GcmMessage;
import com.jdroid.android.sample.gcm.AndroidGcmMessage;
import com.jdroid.java.collections.Maps;

import java.util.Map;

public class AndroidGcmDebugContext extends GcmDebugContext {

	@Override
	protected Map<GcmMessage, Map<String, String>> getGcmMessagesMap() {
		Map<GcmMessage, Map<String, String>> gcmMessagesMap = Maps.newHashMap();
		gcmMessagesMap.put(AndroidGcmMessage.SAMPLE_MESSAGE, null);
		return gcmMessagesMap;
	}
}
