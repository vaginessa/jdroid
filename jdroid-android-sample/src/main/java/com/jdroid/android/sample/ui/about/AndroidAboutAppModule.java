package com.jdroid.android.sample.ui.about;

import com.jdroid.android.about.AboutAppModule;
import com.jdroid.android.about.AboutContext;

public class AndroidAboutAppModule extends AboutAppModule {

	public static AndroidAboutAppModule get() {
		synchronized (AndroidAboutAppModule.class) {
			if (AboutAppModule.INSTANCE == null) {
				AboutAppModule.INSTANCE = new AndroidAboutAppModule();
			}
		}
		return (AndroidAboutAppModule)AboutAppModule.INSTANCE;
	}

	@Override
	protected AboutContext createAboutContext() {
		return new AndroidAboutContext();
	}
}
