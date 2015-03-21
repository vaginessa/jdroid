package com.jdroid.android.uri;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class NoSegmentsUriHandler extends UriHandler<Void> {

	@Override
	public Boolean match(Uri uri) {
		return uri.getPathSegments().isEmpty();
	}

	/**
	 * @see com.jdroid.android.uri.UriHandler#parseParameters(android.net.Uri)
	 */
	@Override
	public Void parseParameters(Uri uri) {
		return null;
	}
	
	/**
	 * @see com.jdroid.android.uri.UriHandler#createStartIntent(android.content.Context, java.lang.Object)
	 */
	@Override
	public Intent createStartIntent(Context context, Void parameters) {
		return null;
	}
}
