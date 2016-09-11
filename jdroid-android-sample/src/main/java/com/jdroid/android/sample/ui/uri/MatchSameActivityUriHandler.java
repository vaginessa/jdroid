package com.jdroid.android.sample.ui.uri;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.jdroid.android.uri.AbstractUriHandler;

public class MatchSameActivityUriHandler extends AbstractUriHandler {
	@Override
	public Boolean matches(Uri uri) {
		return true;
	}

	@Override
	public Intent createMainIntent(Context context, Uri uri) {
		return new Intent(context, MatchSameActivity.class);
	}
}
