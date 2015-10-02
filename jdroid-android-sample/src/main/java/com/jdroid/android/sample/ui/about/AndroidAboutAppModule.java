package com.jdroid.android.sample.ui.about;

import com.jdroid.android.about.AboutAppModule;
import com.jdroid.android.about.AboutContext;

public class AndroidAboutAppModule extends AboutAppModule {

	@Override
	protected AboutContext createAboutContext() {
		return new AndroidAboutContext();
	}
}
