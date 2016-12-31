package com.jdroid.android.crashlytics;

import android.app.Activity;

import com.crashlytics.android.Crashlytics;
import com.jdroid.android.analytics.AbstractCoreAnalyticsTracker;
import com.jdroid.android.context.SecurityContext;
import com.jdroid.android.exception.DefaultExceptionHandler;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class CrashlyticsCoreAnalyticsTracker extends AbstractCoreAnalyticsTracker {
	
	@Override
	public Boolean isEnabled() {
		return CrashlyticsAppModule.get().getCrashlyticsAppContext().isCrashlyticsEnabled();
	}
	
	@Override
	public void onInitExceptionHandler(Map<String, String> metadata) {
		if (metadata != null) {
			for (Entry<String, String> entry : metadata.entrySet()) {
				if (entry.getValue() != null) {
					Crashlytics.getInstance().core.setString(entry.getKey(), entry.getValue());
				}
			}
		}
	}
	
	@Override
	public void trackHandledException(Throwable throwable, List<String> tags) {
		if (areTagsEnabled()) {
			DefaultExceptionHandler.addTags(throwable, tags);
		}
		Crashlytics.getInstance().core.logException(throwable);
	}

	protected Boolean areTagsEnabled() {
		return false;
	}

	@Override
	public void trackErrorBreadcrumb(String message) {
		Crashlytics.getInstance().core.log(message);
	}

	@Override
	public void onActivityStart(Activity activity, String referrer, Object data) {
		if (referrer != null) {
			Crashlytics.getInstance().core.setString("Referrer", referrer);
		}
		
		Crashlytics.getInstance().core.setString("UserId",
				SecurityContext.get().isAuthenticated() ? SecurityContext.get().getUser().getId().toString() : null);

		Crashlytics.getInstance().core.log("Started " + activity.getClass().getSimpleName());
	}
}