package com.jdroid.android.uri;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.jdroid.android.analytics.AppLoadingSource;
import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.notification.NotificationBuilder;
import com.jdroid.java.collections.Lists;
import com.jdroid.java.exception.UnexpectedException;
import com.jdroid.java.utils.LoggerUtils;

import org.slf4j.Logger;

import java.util.List;

/**
 * Mapper which allows to navigate the application using a Uri.
 */
public class UriMapper {

	private static final Logger LOGGER = LoggerUtils.getLogger(UriMapper.class);

	private List<UriHandler> handlers = Lists.newArrayList();

	public void addUriHandler(UriHandler uriHandler) {
		handlers.add(uriHandler);
	}

	public void handleUri(Activity activity, Bundle savedInstanceState, UriHandler uriHandler) {
		if (savedInstanceState == null) {
			Uri uri = activity.getIntent().getData();
			if (uri != null) {
				if (uriHandler != null) {
					String uriString = uri.toString();
					if (uri.getScheme() != null) {
						if (uriString != null && !uriString.startsWith(NotificationBuilder.NOTIFICATION_URI)) {
							Intent intent;
							try {
								if (uriHandler.matches(uri)) {
									LOGGER.debug(uriHandler.getClass().getSimpleName() + " matches the main intent: " + uriString);
									intent = uriHandler.createMainIntent(activity, uri);
									if (intent != null) {
										intent.setData(uri);
										activity.setIntent(intent);
									} else {
										intent = activity.getIntent();
									}
									setTrackingFlags(activity.getClass().getSimpleName(), intent);
								} else {
									AbstractApplication.get().getExceptionHandler().logWarningException(uriHandler.getClass().getSimpleName() + " matches the default intent: " + uriString);
									handleDefaultIntent(activity, uriHandler, uri);
								}
							} catch (Exception e) {
								AbstractApplication.get().getExceptionHandler().logHandledException(new UnexpectedException("Error matching: " + uri, e));
								handleDefaultIntent(activity, uriHandler, uri);
							}
						}
					} else {
						AbstractApplication.get().getExceptionHandler().logHandledException(new UnexpectedException("Uri with null scheme: " + uriString));
					}
				} else {
					AbstractApplication.get().getExceptionHandler().logHandledException(new UnexpectedException("No uriHandler defined for: " + uri.toString()));
				}
			}
		}
	}

	private void handleDefaultIntent(Activity activity, UriHandler uriHandler, Uri uri) {
		Intent intent;
		intent = uriHandler.createDefaultIntent(activity, uri);
		if (intent != null) {
			String className = intent.getComponent().getShortClassName();
			int dot = className.lastIndexOf('.');
			if (dot != -1) {
				className = className.substring(dot + 1);
			}
			setTrackingFlags(className, intent);
			activity.finish();
			activity.startActivity(intent);
		} else {
			intent = activity.getIntent();
			setTrackingFlags(activity.getClass().getSimpleName(), intent);
		}
	}

	private void setTrackingFlags(String screenName, Intent intent) {
		AppLoadingSource appLoadingSource = AppLoadingSource.URL;
		AbstractApplication.get().getAnalyticsSender().trackUriOpened(appLoadingSource.getName(), screenName);
		appLoadingSource.flagIntent(intent);
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

	public Intent getIntentFromUri(Context context, Uri uri) {
		Intent intent = null;
		if (uri != null) {
			UriHandler uriHandler = getUriHandler(uri);
			if (uriHandler != null) {
				try {
					if (uriHandler.matches(uri)) {
						LOGGER.debug(uriHandler.getClass().getSimpleName() + " matches the main intent: " + uri.toString());
						intent = uriHandler.createMainIntent(context, uri);
					} else {
						LOGGER.debug(uriHandler.getClass().getSimpleName() + " matches the default intent: " + uri.toString());
						intent = uriHandler.createDefaultIntent(context, uri);
					}
				} catch (Exception e) {
					AbstractApplication.get().getExceptionHandler().logHandledException(new UnexpectedException("Error matching: " + uri, e));
					intent = uriHandler.createDefaultIntent(context, uri);
				}
			}
		}
		return intent;
	}

	/**
	 * Checks if any UriHandler matches the uri
	 *
	 * @param uri the uri to evaluate.
	 * @return the matched UriHandler or null.
	 */
	private UriHandler getUriHandler(Uri uri) {
		try {
			for (UriHandler uriHandler : handlers) {
				if (uriHandler.matchesIntentFilter(uri)) {
					return uriHandler;
				}
			}
			AbstractApplication.get().getExceptionHandler().logHandledException(new UnexpectedException("No uriHandler matches: " + uri.toString()));
		} catch (Exception e) {
			AbstractApplication.get().getExceptionHandler().logHandledException(new UnexpectedException("Error matching intent filter: " + uri, e));
		}
		return null;
	}
}