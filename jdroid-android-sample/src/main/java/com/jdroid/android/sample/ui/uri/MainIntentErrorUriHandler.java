package com.jdroid.android.sample.ui.uri;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.jdroid.android.uri.AbstractUriHandler;

public class MainIntentErrorUriHandler extends AbstractUriHandler<MainIntentErrorActivity> {
	@Override
	public Boolean matches(Uri uri) {
		return true;
	}

	@Override
	public Intent createMainIntent(Context context, Uri uri) {
		throw new RuntimeException();
	}
}
