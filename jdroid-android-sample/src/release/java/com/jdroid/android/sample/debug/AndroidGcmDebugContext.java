package com.jdroid.android.sample.debug;

import android.os.Bundle;

import com.jdroid.android.google.gcm.GcmDebugContext;
import com.jdroid.android.google.gcm.GcmMessage;

import java.util.Map;

public class AndroidGcmDebugContext extends GcmDebugContext {

	@Override
	public Map<GcmMessage, Bundle> getGcmMessagesMap() {
		return null;
	}
}
