package com.jdroid.sample.android.uri;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.jdroid.android.uri.UriHandler;
import com.jdroid.sample.android.ui.ads.AdsActivity;

public class AdsUriHandler implements UriHandler {

	@Override
	public Boolean matches(Uri uri) {
		return !uri.getPathSegments().isEmpty() && uri.getPathSegments().get(0).equals("ads");
	}

	@Override
	public Intent getStartIntent(Context context, Uri uri) {
		Intent intent = new Intent(context, AdsActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		return intent;
	}
}
