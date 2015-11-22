package com.jdroid.android.sample.debug;

import android.os.Bundle;

import com.jdroid.android.google.gcm.GcmDebugContext;
import com.jdroid.android.google.gcm.GcmMessage;
import com.jdroid.android.sample.gcm.AndroidGcmMessage;
import com.jdroid.java.collections.Maps;

import java.util.Map;

public class AndroidGcmDebugContext extends GcmDebugContext {

	@Override
	protected Map<GcmMessage, Bundle> getGcmMessagesMap() {
		Map<GcmMessage, Bundle> gcmMessagesMap = Maps.newHashMap();
		gcmMessagesMap.put(AndroidGcmMessage.SAMPLE_MESSAGE, null);
		return gcmMessagesMap;
	}
}
