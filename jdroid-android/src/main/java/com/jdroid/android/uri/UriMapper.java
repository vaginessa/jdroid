package com.jdroid.android.uri;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.utils.ReferrerUtils;
import com.jdroid.java.annotation.Internal;
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

	private static final String HTTP_UNDEFINED = "http://undefined";

	private List<UriWatcher> uriWatchers = Lists.newArrayList();

	@Internal
	public UriHandlingResult handleUri(@NonNull Activity activity, @Nullable UriHandler uriHandler) {
		UriHandlingResult uriHandlingResult = new UriHandlingResult();
		Uri uri = UriUtils.getUri(activity);
		if (uri != null && !uri.getScheme().equals("notification")) {
			notifyToUriWatchers(uri);
			if (uriHandler != null) {
				String referrerCategory = ReferrerUtils.getReferrerCategory(activity);
				if (referrerCategory == null) {
					referrerCategory = HTTP_UNDEFINED;
					ReferrerUtils.setReferrer(activity.getIntent(), referrerCategory);
				}
				try {
					if (uriHandler.matches(uri)) {
						LOGGER.debug(uriHandler.getClass().getSimpleName() + " matches the main intent: " + uri.toString());
						Intent intent = uriHandler.createMainIntent(activity, uri);
						handleIntent(intent, activity, uri, referrerCategory, uriHandlingResult);
					} else {
						AbstractApplication.get().getExceptionHandler().logWarningException(uriHandler.getClass().getSimpleName() + " matches the default intent: " + uri.toString());
						handleDefaultIntent(activity, uriHandler, uri, referrerCategory, uriHandlingResult);
					}
				} catch (Exception e) {
					AbstractApplication.get().getExceptionHandler().logHandledException(new UnexpectedException("Error matching: " + uri.toString(), e));
					try {
						handleDefaultIntent(activity, uriHandler, uri, referrerCategory, uriHandlingResult);
					} catch (Exception e2) {
						Intent homeIntent = new Intent(activity, AbstractApplication.get().getHomeActivityClass());
						activity.finish();
						activity.startActivity(homeIntent);
					}
				}
				uriHandlingResult.setUriHandled(true);
			} else {
				AbstractApplication.get().getExceptionHandler().logHandledException(new UnexpectedException("No uriHandler defined for: " + uri.toString()));
			}
		}
		uriHandlingResult.setUriHandled(false);
		return uriHandlingResult;
	}

	private void handleIntent(Intent intent, Activity activity, Uri uri, String referrerCategory, UriHandlingResult uriHandlingResult) {
		if (intent != null) {
			ReferrerUtils.setReferrer(intent, referrerCategory);
			String className = intent.getComponent().getShortClassName();
			int dot = className.lastIndexOf('.');
			if (dot != -1) {
				className = className.substring(dot + 1);
			}
			AbstractApplication.get().getAnalyticsSender().trackUriOpened(className, referrerCategory);
			if (activity.getIntent().getComponent().equals(intent.getComponent())) {
				intent.setData(uri);
				activity.setIntent(intent);
			} else {
				activity.finish();
				activity.startActivity(intent);
				uriHandlingResult.setNewActivityOpened(true);
			}
		} else {
			AbstractApplication.get().getAnalyticsSender().trackUriOpened(activity.getClass().getSimpleName(), referrerCategory);
		}
	}

	private void handleDefaultIntent(Activity activity, UriHandler uriHandler, Uri uri, String referrerCategory, UriHandlingResult uriHandlingResult) {
		Intent intent = uriHandler.createDefaultIntent(activity, uri);
		handleIntent(intent, activity, uri, referrerCategory, uriHandlingResult);
	}

	private void notifyToUriWatchers(final Uri uri) {
		if (!Lists.isNullOrEmpty(uriWatchers)) {
			for (UriWatcher each : uriWatchers) {
				LOGGER.debug("Notifying opened Uri to " + each.getClass().getSimpleName());
				try {
					each.onUriOpened(uri);
				} catch (Exception e) {
					AbstractApplication.get().getExceptionHandler().logHandledException(e);
				}
			}
		}
	}

	public void addUriWatcher(@NonNull UriWatcher uriWatcher) {
		this.uriWatchers.add(uriWatcher);
	}
}