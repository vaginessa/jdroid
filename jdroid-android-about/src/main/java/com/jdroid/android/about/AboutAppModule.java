package com.jdroid.android.about;

import com.jdroid.android.application.AbstractAppModule;
import com.jdroid.android.application.AbstractApplication;

public class AboutAppModule extends AbstractAppModule {

	public static final String MODULE_NAME = AboutAppModule.class.getName();

	public static AboutAppModule get() {
		return (AboutAppModule)AbstractApplication.get().getAppModule(MODULE_NAME);
	}

	private AboutContext aboutContext;

	public AboutAppModule() {
		aboutContext = createAboutContext();
	}

	protected AboutContext createAboutContext() {
		return new AboutContext();
	}

	public AboutContext getAboutContext() {
		return aboutContext;
	}
}