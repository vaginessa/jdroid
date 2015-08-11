package com.jdroid.android.analytics;

import android.content.Intent;

public enum AppLoadingSource {
	
	NORMAL("normal"),
	NOTIFICATION("notification"),
	URL("url"),
	DEEPLINK("deeplink");
	
	private static final String APP_LOADING_SOURCE = "appLoadingSource";
	
	private String name;
	
	AppLoadingSource(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public void flagIntent(Intent intent) {
		intent.putExtra(APP_LOADING_SOURCE, getName());
	}
	
	public static AppLoadingSource getAppLoadingSource(Intent intent) {
		String appLoadingSourceValue = intent.getStringExtra(APP_LOADING_SOURCE);
		for (AppLoadingSource each : values()) {
			if (each.getName().equals(appLoadingSourceValue)) {
				return each;
			}
		}
		return null;
	}
}
