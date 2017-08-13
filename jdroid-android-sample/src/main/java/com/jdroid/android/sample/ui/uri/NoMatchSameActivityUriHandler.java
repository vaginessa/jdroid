package com.jdroid.android.sample.ui.uri;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.jdroid.android.uri.AbstractUriHandler;

public class NoMatchSameActivityUriHandler extends AbstractUriHandler<NoMatchSameActivity> {
	@Override
	public Boolean matches(Uri uri) {
		return false;
	}

	@Override
	public Intent createDefaultIntent(Context context, Uri uri) {
		return new Intent(context, NoMatchSameActivity.class);
	}
}
