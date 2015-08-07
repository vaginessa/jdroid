package com.jdroid.android;

import android.app.Activity;
import android.content.Context;

import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.context.AppContext;
import com.jdroid.android.exception.ExceptionHandler;

public class TestAndroidApplication extends AbstractApplication {

	@Override
	protected void attachBaseContext(Context base) {
		try {
			super.attachBaseContext(base);
		} catch (RuntimeException ignored) {
			// Multidex support doesn't play well with Robolectric yet
		}
	}

	@Override
	public Class<? extends Activity> getHomeActivityClass() {
		return null;
	}

	@Override
	protected AppContext createAppContext() {
		return new TestAppContext();
	}

	@Override
	public Class<? extends ExceptionHandler> getExceptionHandlerClass() {
		return TestExceptionHandler.class;
	}
}
