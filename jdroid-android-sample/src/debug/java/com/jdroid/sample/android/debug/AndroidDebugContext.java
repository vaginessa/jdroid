package com.jdroid.sample.android.debug;

import android.content.Intent;
import android.support.v4.util.Pair;

import com.jdroid.android.debug.DebugContext;
import com.jdroid.android.debug.EmulatedGcmMessageIntentBuilder;
import com.jdroid.android.google.gcm.GcmMessage;
import com.jdroid.java.collections.Lists;
import com.jdroid.java.collections.Maps;
import com.jdroid.java.http.Server;
import com.jdroid.android.sample.api.ApiServer;
import com.jdroid.android.sample.gcm.AndroidGcmMessage;

import java.util.List;
import java.util.Map;

public class AndroidDebugContext extends DebugContext {

	@Override
	public List<Pair<String, Object>> getCustomDebugInfoProperties() {
		List<Pair<String, Object>> customProperties = Lists.newArrayList();
		customProperties.add(new Pair<String, Object>("Sample Key", "Sample Value"));
		return customProperties;
	}

	@Override
	public Map<Class<? extends Server>, List<? extends Server>> getServersMap() {
		Map<Class<? extends Server>, List<? extends Server>> serversMap = Maps.newHashMap();
		serversMap.put(ApiServer.class, Lists.newArrayList(ApiServer.values()));
		return serversMap;
	}

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
