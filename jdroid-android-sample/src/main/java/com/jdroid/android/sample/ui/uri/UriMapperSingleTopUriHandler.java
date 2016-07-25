package com.jdroid.android.sample.ui.uri;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.jdroid.android.uri.AbstractUriHandler;

public class UriMapperSingleTopUriHandler extends AbstractUriHandler {

	@Override
	public Boolean matchesIntentFilter(Uri uri) {
		return uri.getPath().startsWith("/uri/singletop");
	}

	@Override
	public Boolean matches(Uri uri) {
		return uri.getQueryParameter("a").equals("1") || uri.getQueryParameter("a").equals("2");
	}

	@Override
	public Intent createMainIntent(Context context, Uri uri) {
		return new Intent(context, UriMapperSingleTopActivity.class);
	}

	@Override
	public String getUrl(Activity activity) {
		return "http://jdroidframework.com/uri/singletop?a=1";
	}
}
