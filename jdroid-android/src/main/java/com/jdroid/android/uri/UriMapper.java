package com.jdroid.android.uri;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.URLUtil;

import com.jdroid.android.AbstractApplication;
import com.jdroid.android.analytics.AppLoadingSource;
import com.jdroid.android.utils.NotificationBuilder;
import com.jdroid.java.collections.Lists;
import com.jdroid.java.utils.LoggerUtils;

import org.slf4j.Logger;

import java.util.List;

/**
 * Mapper which allows to navigate the application using a Uri.
 * 
 */
public class UriMapper {
	
	private static final String GOOGLE_PLUS_DEEPLINK_PREFFIX = "vnd.google.deeplink://link/?deep_link_id=";
	private static final String GOOGLE_PLUS_DEEPLINK_SUFFIX = "&gplus_source=stream";
	
	private static final Logger LOG = LoggerUtils.getLogger(UriMapper.class);

	private List<UriHandler> handlers = Lists.newArrayList();

	public void addUriHandler(UriHandler uriHandler) {
		handlers.add(uriHandler);
	}
	
	public void handleUri(Activity activity, Bundle savedInstanceState, UriHandler uriHandler) {
		if (savedInstanceState == null) {

			Uri uri = activity.getIntent().getData();
			if (uri != null && uri.toString() != null && !uri.toString().startsWith(NotificationBuilder.NOTIFICATION_URI)) {

				if (uriHandler.matches(uri)) {
					Intent intent = uriHandler.getStartIntent(activity, uri);
					if (intent != null) {
						activity.setIntent(intent);
					}
				} else {
					AbstractApplication.get().getExceptionHandler().logWarningException("Uri doesn't match with the handler: " + uri.toString());
				}

				AppLoadingSource appLoadingSource;
				if (uri.getScheme() == null) {
					appLoadingSource = AppLoadingSource.NORMAL;
					AbstractApplication.get().getExceptionHandler().logWarningException("Uri with null scheme: " + uri.toString());
				} else if (uri.getScheme().startsWith("http")) {
					appLoadingSource = AppLoadingSource.URL;
					AbstractApplication.get().getAnalyticsSender().trackUriOpened(appLoadingSource.getName(), activity.getClass().getSimpleName());
				} else {
					appLoadingSource = AppLoadingSource.DEEPLINK;
					AbstractApplication.get().getAnalyticsSender().trackUriOpened(appLoadingSource.getName(), activity.getClass().getSimpleName());
				}
				appLoadingSource.flagIntent(activity.getIntent());

			} else {
				AppLoadingSource.NORMAL.flagIntent(activity.getIntent());
			}
		}
	}
	
	/**
	 * Starts the activity associated with given Uri if it exists.
	 * 
	 * @param activity The {@link Activity}
	 * @param uri uri to evaluate
	 */
	private void startActivityFromUri(Activity activity, Uri uri) {
		Intent intent = getIntentFromUri(activity, uri);
		if (intent != null) {

			String className = intent.getComponent().getShortClassName();
			int dot = className.lastIndexOf('.');
			if (dot != -1) {
				className = className.substring(dot + 1);
			}

			AppLoadingSource appLoadingSource;
			if (uri.getScheme() == null) {
				appLoadingSource = AppLoadingSource.NORMAL;
				AbstractApplication.get().getExceptionHandler().logWarningException("Uri not supported: " + uri.toString());
			} else if (uri.getScheme().startsWith("http")) {
				appLoadingSource = AppLoadingSource.URL;
				AbstractApplication.get().getAnalyticsSender().trackUriOpened(appLoadingSource.getName(), className);
			} else {
				appLoadingSource = AppLoadingSource.DEEPLINK;
				AbstractApplication.get().getAnalyticsSender().trackUriOpened(appLoadingSource.getName(), className);
			}
			appLoadingSource.flagIntent(activity.getIntent());

			activity.startActivity(intent);

		}
	}
	
	public Intent getIntentFromUri(Context context, Uri uri) {
		Intent intent = null;
		try {
			if (uri != null) {
				
				// Clean the uri if it is from a Google+ deeplink
				String uriString = uri.toString();
				if (uriString.startsWith(GOOGLE_PLUS_DEEPLINK_PREFFIX)) {
					uriString = uriString.replace(GOOGLE_PLUS_DEEPLINK_PREFFIX, "");
					uriString = uriString.replace(GOOGLE_PLUS_DEEPLINK_SUFFIX, "");
					uriString = new String(URLUtil.decode(uriString.getBytes()));
					uri = Uri.parse(uriString);
				}
				
				intent = getIntentFromUriInner(context, uri);
			}
		} catch (Exception e) {
			// Log the crash.
			AbstractApplication.get().getExceptionHandler().logHandledException("Error parsing Uri: " + uri, e);
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
	public Intent getIntentFromUri(Context context, String uriString) {
		return getIntentFromUri(context, uriString != null ? Uri.parse(uriString) : null);
	}
	
	/**
	 * Returns an intent to start the activity associated with given Uri if it exists.
	 * 
	 * @param context context
	 * @param uri uri to evaluate
	 * @return the intent
	 */
	private Intent getIntentFromUriInner(Context context, Uri uri) {
		UriHandler uriHandler = match(uri);

		Intent intent = null;
		if (uriHandler != null) {
			LOG.debug("Handled Uri: " + uri.toString());
			intent = uriHandler.getStartIntent(context, uri);
		} else {
			AbstractApplication.get().getExceptionHandler().logWarningException("Error parsing Uri: " + uri.toString());
		}
		return intent;
	}
	
	/**
	 * Checks if any UriHandler matches the uri
	 *
	 * @param uri the uri to evaluate.
	 * @return the matched UriHandler or null.
	 */
	private UriHandler match(Uri uri) {
		for (UriHandler uriHandler : handlers) {
			if (uriHandler.matches(uri)) {
				return uriHandler;
			}
		}
		return null;
	}
}