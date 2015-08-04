package com.jdroid.android.uri;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class NoSegmentsUriHandler implements UriHandler {

	@Override
	public Boolean matches(Uri uri) {
		return uri.getPathSegments().isEmpty();
	}

	@Override
	public Intent getStartIntent(Context context, Uri uri) {
		return null;
	}
}
