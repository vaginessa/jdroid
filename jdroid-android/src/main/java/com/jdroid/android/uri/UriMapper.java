package com.jdroid.android.uri;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.utils.ReferrerUtils;
import com.jdroid.java.exception.UnexpectedException;
import com.jdroid.java.utils.LoggerUtils;

import org.slf4j.Logger;

/**
 * Mapper which allows to navigate the application using a Uri.
 */
public class UriMapper {

	private static final Logger LOGGER = LoggerUtils.getLogger(UriMapper.class);

	private static final String HTTP_UNDEFINED = "http://undefined";

	public void handleUri(Activity activity, Bundle savedInstanceState, UriHandler uriHandler) {
		if (savedInstanceState == null) {
			Uri uri = UriUtils.getUri(activity);
			if (uri != null && !uri.getScheme().equals("notification")) {
				if (uriHandler != null) {
					String referrerCategory = ReferrerUtils.getReferrerCategory(activity);
					if (referrerCategory == null) {
						referrerCategory = HTTP_UNDEFINED;
						ReferrerUtils.setReferrer(activity.getIntent(), referrerCategory);
					}
					Intent intent;
					try {
						if (uriHandler.matches(uri)) {
							LOGGER.debug(uriHandler.getClass().getSimpleName() + " matches the main intent: " + uri.toString());
							intent = uriHandler.createMainIntent(activity, uri);
							if (intent != null) {
								intent.setData(uri);
								ReferrerUtils.setReferrer(intent, referrerCategory);
								activity.setIntent(intent);
							}
							AbstractApplication.get().getAnalyticsSender().trackUriOpened(referrerCategory, activity.getClass().getSimpleName());
						} else {
							AbstractApplication.get().getExceptionHandler().logWarningException(uriHandler.getClass().getSimpleName() + " matches the default intent: " + uri.toString());
							handleDefaultIntent(activity, uriHandler, uri, referrerCategory);
						}
					} catch (Exception e) {
						AbstractApplication.get().getExceptionHandler().logHandledException(new UnexpectedException("Error matching: " + uri.toString(), e));
						handleDefaultIntent(activity, uriHandler, uri, referrerCategory);
					}
				} else {
					AbstractApplication.get().getExceptionHandler().logHandledException(new UnexpectedException("No uriHandler defined for: " + uri.toString()));
				}
			}
		}
	}

	private void handleDefaultIntent(Activity activity, UriHandler uriHandler, Uri uri, String referrerCategory) {
		Intent intent = uriHandler.createDefaultIntent(activity, uri);
		if (intent != null) {
			ReferrerUtils.setReferrer(intent, referrerCategory);
			String className = intent.getComponent().getShortClassName();
			int dot = className.lastIndexOf('.');
			if (dot != -1) {
				className = className.substring(dot + 1);
			}
			AbstractApplication.get().getAnalyticsSender().trackUriOpened(referrerCategory, className);
			activity.finish();
			activity.startActivity(intent);
		} else {
			AbstractApplication.get().getAnalyticsSender().trackUriOpened(referrerCategory, activity.getClass().getSimpleName());
		}
	}
}