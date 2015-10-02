package com.jdroid.android.facebook;

import com.jdroid.android.application.AbstractAppModule;
import com.jdroid.android.application.AbstractApplication;

public class FacebookAppModule extends AbstractAppModule {

	public static final String MODULE_NAME = FacebookAppModule.class.getName();

	public static FacebookAppModule get() {
		return (FacebookAppModule)AbstractApplication.get().getAppModule(MODULE_NAME);
	}

	private FacebookContext facebookContext;

	public FacebookAppModule() {
		facebookContext = createFacebookContext();
	}

	protected FacebookContext createFacebookContext() {
		return new FacebookContext();
	}

	public FacebookContext getFacebookContext() {
		return facebookContext;
	}
}