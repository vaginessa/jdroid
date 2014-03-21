package com.jdroid.android.uri;

import org.slf4j.Logger;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import com.jdroid.android.AbstractApplication;
import com.jdroid.java.utils.LoggerUtils;

/**
 * Mapper which allows to navigate the application using a Uri.
 * 
 */
public class UriMapper {
	
	/**
	 * In case of a valid Uri, we should send this value as invalidUri parameter.
	 */
	private static final String TRACKING_PARAM_VALID = "valid";
	/**
	 * In case of an invalid Uri, we should send this value as validUri parameter.
	 */
	private static final String TRACKING_PARAM_INVALID = "invalid";
	private static final Logger LOG = LoggerUtils.getLogger(UriMapper.class);
	private static final PathPrefixMatcher PATH_PREFIX_MATCHER = buildPathMatcher();
	
	/**
	 * Build the matcher to evaluate Uris.
	 * 
	 * @return the matcher.
	 */
	private static PathPrefixMatcher buildPathMatcher() {
		PathPrefixMatcher matcher = new PathPrefixMatcher();
		matcher.setNoMatchObject(new NoMatchUriHandler());
		return matcher;
	}
	
	public static void addUriHandler(UriHandler<?> uriHandler) {
		PATH_PREFIX_MATCHER.addUriHandler(uriHandler);
	}
	
	/**
	 * Starts the activity associated with given Uri if it exists.
	 * 
	 * @param context context
	 * @param uri uri to evaluate
	 */
	public static void startActivityFromUri(Context context, Uri uri) {
		context.startActivity(getIntentFromUri(context, uri));
	}
	
	public static void startActivityFromUri(Context context, String uriString) {
		context.startActivity(getIntentFromUri(context, uriString));
	}
	
	public static Intent getIntentFromUri(Context context, Uri uri) {
		Intent intent = null;
		try {
			if (uri != null) {
				intent = getIntentFromUriInner(context, uri);
			} else {
				intent = createDefaultIntent(context, uri);
			}
		} catch (Exception e) {
			// Log the crash.
			AbstractApplication.get().getExceptionHandler().logHandledException("Error parsing Uri: " + uri, e);
			// Return default intent
			intent = createDefaultIntent(context, uri);
		}
		return intent;
	}
	
	/**
	 * Returns an intent to start the activity associated with given Uri if it exists.
	 * 
	 * @param context context
	 * @param uriString uri to evaluate
	 * @return the intent
	 */
	public static Intent getIntentFromUri(Context context, String uriString) {
		return getIntentFromUri(context, uriString != null ? Uri.parse(uriString) : null);
	}
	
	/**
	 * Returns an intent to start the activity associated with given Uri if it exists.
	 * 
	 * @param context context
	 * @param uri uri to evaluate
	 * @return the intent
	 */
	private static Intent getIntentFromUriInner(Context context, Uri uri) {
		UriHandler<?> uriHandler = PATH_PREFIX_MATCHER.match(uri);
		Intent intent = uriHandler.getStartIntent(context, uri);
		boolean handled = !PATH_PREFIX_MATCHER.isNoMatchObject(uriHandler);
		String trackingParam = uriHandler.createTrackingParam(uri);
		
		// Track the event.
		if (handled) {
			AbstractApplication.get().getAnalyticsSender().trackUriHandled(handled, trackingParam, TRACKING_PARAM_VALID);
		} else {
			AbstractApplication.get().getAnalyticsSender().trackUriHandled(handled, TRACKING_PARAM_INVALID,
				trackingParam);
		}
		LOG.debug("handled: " + handled + " - trackingParam: " + trackingParam + " - uri: " + uri.toString());
		return intent;
	}
	
	/**
	 * Create a default intent which open home activity.
	 * 
	 * @param context
	 * @param uri
	 * @return the intent
	 */
	private static Intent createDefaultIntent(Context context, Uri uri) {
		Intent intent = new Intent(context, AbstractApplication.get().getHomeActivityClass());
		if (uri != null) {
			String uriString = uri.toString();
			// Track the event.
			AbstractApplication.get().getAnalyticsSender().trackUriHandled(false, TRACKING_PARAM_INVALID, uriString);
			LOG.debug("handled: " + false + " - uri: " + uriString);
		}
		return intent;
	}
}