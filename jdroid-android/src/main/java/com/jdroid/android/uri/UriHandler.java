package com.jdroid.android.uri;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/**
 * Handler which parse the parameters and starts the proper activity based on an uri
 * 
 * @param <T> parameters
 * 
 */
public abstract class UriHandler<T> {
	
	/**
	 * @return Whether the uri matches the handler or not
	 */
	public abstract Boolean match(Uri uri);
	
	/**
	 * Parse the parameters and create the intent for the proper activity base on the uri.
	 * 
	 * @param context the context.
	 * @param uri the uri to handle.
	 * @return the Intent.
	 */
	public Intent getStartIntent(Context context, Uri uri) {
		// Parse parameters
		T parameters = parseParameters(uri);
		
		// Start activity
		return createStartIntent(context, parameters);
	}
	
	/**
	 * Parse and populate the parameters needed to start the activity.
	 * 
	 * @param uri uri to parse.
	 * @return the result of the parameter parsing.
	 */
	protected abstract T parseParameters(Uri uri);
	
	/**
	 * Creates a intent to start the activity.
	 * 
	 * @param context
	 * @param parameters
	 * @return the intent
	 */
	protected abstract Intent createStartIntent(Context context, T parameters);
}
