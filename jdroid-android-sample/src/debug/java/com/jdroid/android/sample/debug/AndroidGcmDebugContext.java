package com.jdroid.android.sample.debug;

import android.content.Intent;

import com.jdroid.android.google.gcm.EmulatedGcmMessageIntentBuilder;
import com.jdroid.android.google.gcm.GcmDebugContext;
import com.jdroid.android.google.gcm.GcmMessage;
import com.jdroid.android.sample.gcm.AndroidGcmMessage;
import com.jdroid.java.collections.Maps;

import java.util.Map;

public class AndroidGcmDebugContext extends GcmDebugContext {

	@Override
	public Map<GcmMessage, EmulatedGcmMessageIntentBuilder> getGcmMessagesMap() {
		Map<GcmMessage, EmulatedGcmMessageIntentBuilder> gcmMessagesMap = Maps.newHashMap();
		gcmMessagesMap.put(AndroidGcmMessage.SAMPLE_MESSAGE, new EmulatedGcmMessageIntentBuilder() {

			@Override
			public Intent buildIntent() {
				return new Intent();
			}
		});
		return gcmMessagesMap;
	}
}
