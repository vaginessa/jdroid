package com.jdroid.android.uil.debug;

import android.content.Context;

import com.jdroid.android.lifecycle.ApplicationLifecycleCallback;
import com.jdroid.android.debug.DebugSettingsHelper;

public class UilDebugAppLifecycleCallback extends ApplicationLifecycleCallback {
	
	@Override
	public void onProviderInit(Context context) {
		super.onProviderInit(context);
		
		DebugSettingsHelper.addPreferencesAppender(new ImageLoaderDebugPrefsAppender());
	}
}
