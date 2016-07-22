package com.jdroid.android.uri;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.jdroid.android.application.AbstractApplication;

public abstract class AbstractUriHandler implements UriHandler {

	@Override
	public Boolean matches(Uri uri) {
		return true;
	}

	@Override
	public Intent createDefaultIntent(Context context, Uri uri) {
		return new Intent(context, AbstractApplication.get().getHomeActivityClass());
	}
}
