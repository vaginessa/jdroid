package com.jdroid.android.sample.ui.home;

import com.jdroid.android.uri.AbstractUriHandler;

public class HomeUriHandler extends AbstractUriHandler<HomeActivity> {

	@Override
	public String getUrl(HomeActivity activity) {
		return "http://jdroidtools.com/";
	}

	@Override
	public Boolean isAppIndexingEnabled(HomeActivity activity) {
		return false;
	}
}
