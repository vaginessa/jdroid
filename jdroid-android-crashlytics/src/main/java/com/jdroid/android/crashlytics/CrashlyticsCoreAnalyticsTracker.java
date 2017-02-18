package com.jdroid.android.crashlytics;

import android.app.Activity;

import com.crashlytics.android.Crashlytics;
import com.jdroid.android.analytics.AbstractCoreAnalyticsTracker;
import com.jdroid.android.context.SecurityContext;
import com.jdroid.android.exception.DefaultExceptionHandler;
import com.jdroid.android.uri.ReferrerUtils;

import java.util.List;

public class CrashlyticsCoreAnalyticsTracker extends AbstractCoreAnalyticsTracker {
	
	@Override
	public Boolean isEnabled() {
		return CrashlyticsAppModule.get().getCrashlyticsAppContext().isCrashlyticsEnabled();
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
		if (!ReferrerUtils.isUndefined(referrer)) {
			Crashlytics.getInstance().core.setString("Referrer", referrer);
		}
		
		Crashlytics.getInstance().core.setString("UserId",
				SecurityContext.get().isAuthenticated() ? SecurityContext.get().getUser().getId().toString() : null);

		Crashlytics.getInstance().core.log("Started " + activity.getClass().getSimpleName());
	}
}