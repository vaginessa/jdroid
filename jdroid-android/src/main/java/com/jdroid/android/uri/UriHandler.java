package com.jdroid.android.uri;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import com.jdroid.android.AbstractApplication;

/**
 * Handler which parse the parameters and starts the proper activity based on an uri
 * 
 * @param <T> parameters
 * 
 */
public abstract class UriHandler<T> {
	
	/**
	 * Return the path prefixes associated with this handler.
	 * 
	 * @return the path prefix
	 */
	public abstract String[] getPathPrefixes();
	
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
	 * Starts a default activity for landing.
	 * 
	 * @param context
	 */
	protected void startDefaultActivity(Context context) {
		context.startActivity(createDefaultIntent(context));
	}
	
	/**
	 * Creates a default intent for to start the landing activity.
	 * 
	 * @param context
	 * @return the intent
	 */
	protected Intent createDefaultIntent(Context context) {
		return new Intent(context, AbstractApplication.get().getHomeActivityClass());
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
