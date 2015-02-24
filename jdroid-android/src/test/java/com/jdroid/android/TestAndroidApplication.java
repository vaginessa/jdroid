package com.jdroid.android;

import android.app.Activity;

import com.jdroid.android.context.AppContext;

public class TestAndroidApplication extends AbstractApplication {

	@Override
	public Class<? extends Activity> getHomeActivityClass() {
		return null;
	}

	@Override
	protected AppContext createAppContext() {
		return new TestAppContext();
	}
}
