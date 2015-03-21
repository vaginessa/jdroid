package com.jdroid.sample.android.uri;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.jdroid.android.uri.UriHandler;
import com.jdroid.sample.android.ui.ads.AdsActivity;

public class AdsUriHandler extends UriHandler<Void> {

	@Override
	public Boolean match(Uri uri) {
		return !uri.getPathSegments().isEmpty() && uri.getPathSegments().get(0).equals("ads");
	}

	/**
	 * @see UriHandler#parseParameters(Uri)
	 */
	@Override
	public Void parseParameters(Uri uri) {
		return null;
	}

	/**
	 * @see UriHandler#createStartIntent(Context, Object)
	 */
	@Override
	public Intent createStartIntent(Context context, Void parameters) {
		Intent intent = new Intent(context, AdsActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		return intent;
	}
}
