package com.jdroid.android.facebook;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.jdroid.android.application.AbstractAppModule;
import com.jdroid.android.application.AbstractApplication;

public class FacebookAppModule extends AbstractAppModule {

	public static final String MODULE_NAME = FacebookAppModule.class.getName();

	public static FacebookAppModule get() {
		return (FacebookAppModule)AbstractApplication.get().getAppModule(MODULE_NAME);
	}

	@Override
	public void onCreate() {
		FacebookSdk.sdkInitialize(AbstractApplication.get().getApplicationContext());
		if (analyticsEnabled()) {
			AppEventsLogger.activateApp(AbstractApplication.get());
		}
	}

	protected Boolean analyticsEnabled() {
		return true;
	}
}