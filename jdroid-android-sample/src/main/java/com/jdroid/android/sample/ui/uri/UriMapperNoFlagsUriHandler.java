package com.jdroid.android.sample.ui.uri;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.jdroid.android.sample.ui.home.HomeActivity;
import com.jdroid.android.uri.AbstractUriHandler;

public class UriMapperNoFlagsUriHandler extends AbstractUriHandler {

	@Override
	public Boolean matchesIntentFilter(Uri uri) {
		return uri.getPath().startsWith("/uri/noflags");
	}

	@Override
	public Boolean matches(Uri uri) {
		return uri.getQueryParameter("a").equals("1") || uri.getQueryParameter("a").equals("2");
	}

	@Override
	public Intent createMainIntent(Context context, Uri uri) {
		return new Intent(context, UriMapperNoFlagsActivity.class);
	}

	@Override
	public Intent createDefaultIntent(Context context, Uri uri) {
		return new Intent(context, HomeActivity.class);
	}
}
