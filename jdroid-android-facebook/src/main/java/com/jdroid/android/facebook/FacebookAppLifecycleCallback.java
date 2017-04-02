package com.jdroid.android.facebook;

import android.content.Context;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.application.lifecycle.ApplicationLifecycleCallback;
import com.jdroid.android.context.BuildConfigUtils;
import com.jdroid.java.utils.LoggerUtils;

import org.slf4j.Logger;

public class FacebookAppLifecycleCallback extends ApplicationLifecycleCallback {
	
	private final static Logger LOGGER = LoggerUtils.getLogger(FacebookAppLifecycleCallback.class);
	
	@Override
	public void onCreate(Context context) {
		FacebookSdk.sdkInitialize(AbstractApplication.get().getApplicationContext());
		if (BuildConfigUtils.getBuildConfigBoolean("FACEBOOK_ANALYTICS_ENABLED", true)) {
			AppEventsLogger.activateApp(AbstractApplication.get());
		}
	}
}
