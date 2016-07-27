package com.jdroid.android.sample.ui.uri;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.jdroid.android.uri.AbstractUriHandler;

public class UriMapperUriHandler extends AbstractUriHandler {

	@Override
	public Boolean matches(Uri uri) {
		return true;
	}

	@Override
	public Intent createMainIntent(Context context, Uri uri) {
		return new Intent(context, UriMapperActivity.class);
	}

	@Override
	public String getUrl(Activity activity) {
		return "http://jdroidframework.com/uri";
	}
}
