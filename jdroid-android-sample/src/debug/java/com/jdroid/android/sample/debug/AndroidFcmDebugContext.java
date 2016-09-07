package com.jdroid.android.sample.debug;

import com.jdroid.android.firebase.fcm.FcmDebugContext;
import com.jdroid.android.firebase.fcm.FcmMessage;
import com.jdroid.android.sample.fcm.AndroidFcmMessage;
import com.jdroid.java.collections.Maps;

import java.util.Map;

public class AndroidFcmDebugContext extends FcmDebugContext {

	@Override
	protected Map<FcmMessage, Map<String, String>> getFcmMessagesMap() {
		Map<FcmMessage, Map<String, String>> fcmMessagesMap = Maps.newHashMap();
		fcmMessagesMap.put(AndroidFcmMessage.SAMPLE_MESSAGE, null);
		return fcmMessagesMap;
	}
}
