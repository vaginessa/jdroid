package com.jdroid.android;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.context.AppContext;
import com.jdroid.android.debug.DebugContext;
import com.jdroid.android.exception.ExceptionHandler;
import com.jdroid.android.http.HttpConfiguration;
import com.jdroid.java.http.okhttp.OkHttpServiceFactory;

public class TestAndroidApplication extends AbstractApplication {

	public TestAndroidApplication() {
		HttpConfiguration.setHttpServiceFactory(new OkHttpServiceFactory());
	}
	
	@Override
	protected void onInitMultiDex() {
		// Multidex support doesn't play well with Robolectric yet
	}
	
	@Override
	protected Boolean isMultiProcessSupportEnabled() {
		return false;
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
	public int getLauncherIconResId() {
		return 0;
	}

	@Override
	public int getNotificationIconResId() {
		return 0;
	}

	@Override
	public String getManifestPackageName() {
		return null;
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

