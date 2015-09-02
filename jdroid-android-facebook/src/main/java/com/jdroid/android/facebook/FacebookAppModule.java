package com.jdroid.android.facebook;

import com.jdroid.android.application.AbstractAppModule;

public class FacebookAppModule extends AbstractAppModule {

	private static final FacebookAppModule INSTANCE = new FacebookAppModule();

	private FacebookContext facebookContext;

	public static FacebookAppModule get() {
		return INSTANCE;
	}

	private FacebookAppModule() {
		facebookContext = createFacebookContext();
	}

	protected FacebookContext createFacebookContext() {
		return new FacebookContext();
	}

	public FacebookContext getFacebookContext() {
		return facebookContext;
	}
}