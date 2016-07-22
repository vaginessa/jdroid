package com.jdroid.android.uri;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/**
 * Handler which parse the parameters and create an intent based on an uri
 */
public interface UriHandler {

	/**
	 * @return Whether the uri matches the Intent Filter defined on manifest
	 */
	public Boolean matchesIntentFilter(Uri uri);

	/**
	 * @return Whether the uri should be transformed to a main or a default intent
	 */
	public Boolean matches(Uri uri);

	/**
	 * Create the intent for the proper activity based on the uri.
	 *
	 * @param context the context.
	 * @param uri the uri to handle.
	 * @return the Intent.
	 */
	public Intent createMainIntent(Context context, Uri uri);

	public Intent createDefaultIntent(Context context, Uri uri);
}
