package com.jdroid.android.uri;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/**
 * Handler which parse the parameters and starts the proper activity based on an uri
 * 
 */
public interface UriHandler {
	
	/**
	 * @return Whether the uri matches the handler or not
	 */
	public Boolean matches(Uri uri);

	/**
	 * Create the intent for the proper activity base on the uri.
	 *
	 * @param context the context.
	 * @param uri the uri to handle.
	 * @return the Intent.
	 */
	public Intent getStartIntent(Context context, Uri uri);
}
