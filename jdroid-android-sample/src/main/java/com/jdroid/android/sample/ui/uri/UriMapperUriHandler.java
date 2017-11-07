package com.jdroid.android.sample.ui.uri;

import com.jdroid.android.uri.AbstractUriHandler;

public class UriMapperUriHandler extends AbstractUriHandler<UriMapperActivity> {

	@Override
	public String getUrl(UriMapperActivity activity) {
		return "http://jdroidtools.com/uri";
	}
}
