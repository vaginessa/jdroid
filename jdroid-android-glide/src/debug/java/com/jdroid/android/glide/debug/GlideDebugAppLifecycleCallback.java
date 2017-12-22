package com.jdroid.android.glide.debug;

import android.content.Context;

import com.jdroid.android.lifecycle.ApplicationLifecycleCallback;
import com.jdroid.android.debug.DebugSettingsHelper;

public class GlideDebugAppLifecycleCallback extends ApplicationLifecycleCallback {
	
	@Override
	public void onProviderInit(Context context) {
		super.onProviderInit(context);
		
		DebugSettingsHelper.addPreferencesAppender(new GlideDebugPrefsAppender());
	}
}
