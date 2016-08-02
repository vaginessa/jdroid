package com.jdroid.android.uri;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.google.android.gms.appindexing.Action;

/**
 * Handler which parse the parameters and create an intent based on an uri
 */
public interface UriHandler {

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

	public Boolean isAppIndexingEnabled(Activity activity);

	public Action getAppIndexingAction(Activity activity);

	public String getUrl(Activity activity);
}
