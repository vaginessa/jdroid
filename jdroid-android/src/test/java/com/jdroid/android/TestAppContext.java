package com.jdroid.android;

import com.jdroid.android.context.AppContext;

public class TestAppContext extends AppContext {

	public static Boolean FAKE_HTTP_ENABLED = true;

	@Override
	protected String getServerName() {
		return null;
	}

	@Override
	public String getBuildType() {
		return "test";
	}

	@Override
	public Boolean isGoogleAnalyticsEnabled() {
		return false;
	}

	@Override
	public String getGoogleAnalyticsTrackingId() {
		return null;
	}

	@Override
	public Boolean isGoogleAnalyticsDebugEnabled() {
		return null;
	}

	@Override
	public String getLocalIp() {
		return null;
	}

	@Override
	public Boolean isHttpMockEnabled() {
		return FAKE_HTTP_ENABLED;
	}
}
