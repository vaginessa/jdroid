package com.jdroid.android.about;

import com.jdroid.android.application.AbstractAppModule;

public class AboutAppModule extends AbstractAppModule {

	protected static AboutAppModule INSTANCE;

	private AboutContext aboutContext;

	public static AboutAppModule get() {
		return INSTANCE;
	}

	public AboutAppModule() {
		INSTANCE = this;
		aboutContext = createAboutContext();
	}

	protected AboutContext createAboutContext() {
		return new AboutContext();
	}

	public AboutContext getAboutContext() {
		return aboutContext;
	}
}