package com.jdroid.android.sample.ui.home;

import android.app.Activity;

import com.jdroid.android.uri.AbstractUriHandler;

public class HomeUriHandler extends AbstractUriHandler {

	@Override
	public String getUrl(Activity activity) {
		return "http://jdroidframework.com/";
	}

	@Override
	public Boolean isAppIndexingEnabled(Activity activity) {
		return false;
	}
}
