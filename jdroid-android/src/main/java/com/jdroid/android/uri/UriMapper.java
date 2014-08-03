package com.jdroid.android.uri;

import org.slf4j.Logger;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.webkit.URLUtil;
import com.jdroid.android.AbstractApplication;
import com.jdroid.android.analytics.AppLoadingSource;
import com.jdroid.java.utils.LoggerUtils;

/**
 * Mapper which allows to navigate the application using a Uri.
 * 
 */
public class UriMapper {
	
	private static final String GOOGLE_PLUS_DEEPLINK_PREFFIX = "vnd.google.deeplink://link/?deep_link_id=";
	private static final String GOOGLE_PLUS_DEEPLINK_SUFFIX = "&gplus_source=stream";
	
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
	 * Check for an incoming deep link
	 * 
	 * @param activity
	 */
	public static void checkDeepLink(Activity activity) {
		Uri targetUri = activity.getIntent().getData();
		if (targetUri != null) {
			UriMapper.startActivityFromUri(activity, targetUri);
		} else {
			AppLoadingSource.NORMAL.flagIntent(activity.getIntent());
		}
	}
	
	/**
	 * Starts the activity associated with given Uri if it exists.
	 * 
	 * @param activity The {@link Activity}
	 * @param uri uri to evaluate
	 */
	private static void startActivityFromUri(Activity activity, Uri uri) {
		Intent intent = getIntentFromUri(activity, uri);
		
		AppLoadingSource appLoadingSource = null;
		if (uri.getScheme().startsWith("http")) {
			appLoadingSource = AppLoadingSource.URL;
		} else {
			appLoadingSource = AppLoadingSource.DEEPLINK;
		}
		appLoadingSource.flagIntent(activity.getIntent());
		
		String className = intent.getComponent().getShortClassName();
		int dot = className.lastIndexOf('.');
		if (dot != -1) {
			className = className.substring(dot + 1);
		}
		AbstractApplication.get().getAnalyticsSender().trackUriOpened(appLoadingSource.getName(), className);
		
		activity.startActivity(intent);
	}
	
	public static Intent getIntentFromUri(Context context, Uri uri) {
		Intent intent = null;
		try {
			if (uri != null) {
				
				// Clean the uri if it is from a Google+ deeplink
				String uriString = uri.toString();
				if (uriString.startsWith(GOOGLE_PLUS_DEEPLINK_PREFFIX)) {
					uriString = uriString.replace(GOOGLE_PLUS_DEEPLINK_PREFFIX, "");
					uriString = uriString.replace(GOOGLE_PLUS_DEEPLINK_SUFFIX, "");
					uriString = new String(URLUtil.decode(uriString.getBytes())).toString();
					uri = Uri.parse(uriString);
				}
				
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
		
		// Track the event.
		if (PATH_PREFIX_MATCHER.isNoMatchObject(uriHandler)) {
			AbstractApplication.get().getExceptionHandler().logWarningException("Error parsing Uri: " + uri.toString());
		} else {
			LOG.debug("Handled Uri: " + uri.toString());
		}
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
			AbstractApplication.get().getExceptionHandler().logWarningException("Error parsing Uri: " + uri.toString());
		}
		intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		return intent;
	}
}