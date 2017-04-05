package com.jdroid.android.firebase.fcm;

import android.content.Context;

import com.jdroid.android.application.lifecycle.ApplicationLifecycleCallback;
import com.jdroid.android.debug.DebugSettingsHelper;

import java.util.Map;

public abstract class AbstractFcmDebugAppLifecycleCallback extends ApplicationLifecycleCallback {
	
	@Override
	public void onProviderInit(Context context) {
		DebugSettingsHelper.addPreferencesAppender(new FcmDebugPrefsAppender(getFcmMessagesMap()));
	}
	
	protected abstract Map<FcmMessage, Map<String, String>> getFcmMessagesMap();
	
}
