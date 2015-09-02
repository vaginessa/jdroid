package com.jdroid.android.application;

import android.content.Context;
import android.content.res.Configuration;

public class AbstractAppModule implements AppModule {

	@Override
	public void onCreate() {
		// Do Nothing
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// Do Nothing
	}

	@Override
	public void onLowMemory() {
		// Do Nothing
	}

	@Override
	public void onTrimMemory(int level) {
		// Do Nothing
	}

	@Override
	public void attachBaseContext(Context base) {
		// Do Nothing
	}
}
