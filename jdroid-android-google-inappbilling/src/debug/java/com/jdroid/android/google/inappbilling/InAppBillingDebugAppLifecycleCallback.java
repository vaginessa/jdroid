package com.jdroid.android.google.inappbilling;

import android.content.Context;

import com.jdroid.android.lifecycle.ApplicationLifecycleCallback;
import com.jdroid.android.debug.DebugSettingsHelper;

public class InAppBillingDebugAppLifecycleCallback extends ApplicationLifecycleCallback {
	
	@Override
	public void onProviderInit(Context context) {
		DebugSettingsHelper.addPreferencesAppender(new InAppBillingDebugPrefsAppender());
	}
	
}
