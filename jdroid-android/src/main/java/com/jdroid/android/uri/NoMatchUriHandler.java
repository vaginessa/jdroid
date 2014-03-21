package com.jdroid.android.uri;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/**
 * Handler which starts Home activity.
 * 
 */
public class NoMatchUriHandler extends UriHandler<Void> {
	
	/**
	 * @see com.jdroid.android.uri.UriHandler#getPathPrefixes()
	 */
	@Override
	public String[] getPathPrefixes() {
		return new String[0];
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
		return createDefaultIntent(context);
	}
}
