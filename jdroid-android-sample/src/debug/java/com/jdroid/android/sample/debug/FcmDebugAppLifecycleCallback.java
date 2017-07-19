package com.jdroid.android.sample.debug;

import com.jdroid.android.firebase.fcm.AbstractFcmDebugAppLifecycleCallback;
import com.jdroid.android.firebase.fcm.FcmMessage;
import com.jdroid.android.sample.firebase.fcm.AndroidFcmMessage;
import com.jdroid.java.collections.Maps;

import java.util.Map;

public class FcmDebugAppLifecycleCallback extends AbstractFcmDebugAppLifecycleCallback {
	@Override
	protected Map<FcmMessage, Map<String, String>> getFcmMessagesMap() {
		Map<FcmMessage, Map<String, String>> fcmMessagesMap = Maps.newHashMap();
		fcmMessagesMap.put(AndroidFcmMessage.SAMPLE_MESSAGE, null);
		return fcmMessagesMap;
	}
}
