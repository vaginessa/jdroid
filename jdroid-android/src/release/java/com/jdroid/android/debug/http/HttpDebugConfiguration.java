package com.jdroid.android.debug.http;

public class HttpDebugConfiguration {
	
	public static boolean isHttpMockEnabled() {
		return false;
	}
	
	public static void setHttpMockEnabled(boolean enabled) {
		
	}
	
	public Integer getHttpMockSleepDuration() {
		return null;
	}
	
	public static AbstractMockHttpService getAbstractMockHttpServiceInstance(Object... urlSegments) {
		return null;
	}
}
