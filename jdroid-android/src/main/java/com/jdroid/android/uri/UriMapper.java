package com.jdroid.android.uri;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.jdroid.android.analytics.AppLoadingSource;
import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.notification.NotificationBuilder;
import com.jdroid.java.exception.UnexpectedException;
import com.jdroid.java.utils.LoggerUtils;

import org.slf4j.Logger;

/**
 * Mapper which allows to navigate the application using a Uri.
 */
public class UriMapper {

	private static final Logger LOGGER = LoggerUtils.getLogger(UriMapper.class);

	public void handleUri(Activity activity, Bundle savedInstanceState, UriHandler uriHandler) {
		if (savedInstanceState == null) {
			Uri uri = activity.getIntent().getData();
			if (uri != null) {
				String uriString = uri.toString();
				if (uriString != null) {
					if (activity.getIntent().hasExtra(NotificationBuilder.ORIGINAL_URL)) {
						uriString = activity.getIntent().getStringExtra(NotificationBuilder.ORIGINAL_URL);
					}
					if (uriHandler != null) {
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
							AbstractApplication.get().getExceptionHandler().logHandledException(new UnexpectedException("Error matching: " + uriString, e));
							handleDefaultIntent(activity, uriHandler, uri);
						}
					} else {
						AbstractApplication.get().getExceptionHandler().logHandledException(new UnexpectedException("No uriHandler defined for: " + uriString));
					}
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
}