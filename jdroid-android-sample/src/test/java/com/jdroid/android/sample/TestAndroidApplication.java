package com.jdroid.android.sample;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;

import com.jdroid.android.analytics.AnalyticsTracker;
import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.context.AppContext;
import com.jdroid.android.debug.DebugContext;
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

	@NonNull
	@Override
	protected AppContext createAppContext() {
		return new TestAppContext();
	}

	@Override
	protected DebugContext createDebugContext() {
		return new TestDebugContext();
	}

	@Override
	public Class<? extends ExceptionHandler> getExceptionHandlerClass() {
		return TestExceptionHandler.class;
	}

	@Override
	public AnalyticsTracker createGoogleAnalyticsTracker() {
		return null;
	}

	@Override
	protected AnalyticsTracker createFirebaseAnalyticsTracker() {
		return null;
	}

	@Override
	public int getLauncherIconResId() {
		return 0;
	}

	@Override
	public void initExceptionHandlers() {
		// Do Nothing
	}

	@Override
	protected void verifyAppLaunchStatus() {
		// Do Nothing
	}
}
