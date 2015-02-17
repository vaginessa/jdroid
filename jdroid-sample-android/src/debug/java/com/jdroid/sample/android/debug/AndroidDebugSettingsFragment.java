package com.jdroid.sample.android.debug;

import android.content.Intent;

import com.jdroid.android.debug.DebugSettingsFragment;
import com.jdroid.android.debug.EmulatedGcmMessageIntentBuilder;
import com.jdroid.android.gcm.GcmMessage;
import com.jdroid.java.collections.Lists;
import com.jdroid.java.http.Server;
import com.jdroid.sample.android.api.ApiServer;
import com.jdroid.sample.android.gcm.AndroidGcmMessage;

import java.util.List;
import java.util.Map;

public class AndroidDebugSettingsFragment extends DebugSettingsFragment {
	
	/**
	 * @see com.jdroid.android.debug.DebugSettingsFragment#initServers(java.util.Map)
	 */
	@Override
	protected void initServers(Map<Class<? extends Server>, List<? extends Server>> serversMap) {
		serversMap.put(ApiServer.class, Lists.newArrayList(ApiServer.values()));
	}
	
	/**
	 * @see com.jdroid.android.debug.DebugSettingsFragment#initGcmMessages(java.util.Map)
	 */
	@Override
	protected void initGcmMessages(Map<GcmMessage, EmulatedGcmMessageIntentBuilder> gcmMessagesMap) {
		gcmMessagesMap.put(AndroidGcmMessage.SAMPLE_MESSAGE, new EmulatedGcmMessageIntentBuilder() {
			
			@Override
			public Intent buildIntent() {
				return new Intent();
			}
		});
	}
}
