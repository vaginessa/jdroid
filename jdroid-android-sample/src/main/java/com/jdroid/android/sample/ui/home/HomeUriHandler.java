package com.jdroid.android.sample.ui.home;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.jdroid.android.uri.AbstractUriHandler;

public class HomeUriHandler extends AbstractUriHandler {

	@Override
	public Boolean matches(Uri uri) {
		return true;
	}

	@Override
	public Intent createMainIntent(Context context, Uri uri) {
		return new Intent(context, HomeActivity.class);
	}
}
