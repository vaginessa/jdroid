package com.jdroid.sample.android;

import com.jdroid.android.context.AppContext;

public class TestAppContext extends AppContext {

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
		return null;
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
	public Boolean isCrashlyticsEnabled() {
		return null;
	}

	@Override
	public Boolean isCrashlyticsDebugEnabled() {
		return null;
	}
}
