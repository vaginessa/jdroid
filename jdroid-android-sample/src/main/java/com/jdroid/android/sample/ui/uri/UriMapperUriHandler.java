package com.jdroid.android.sample.ui.uri;

import android.app.Activity;

import com.jdroid.android.uri.AbstractUriHandler;

public class UriMapperUriHandler extends AbstractUriHandler {

	@Override
	public String getUrl(Activity activity) {
		return "http://jdroidframework.com/uri";
	}
}
