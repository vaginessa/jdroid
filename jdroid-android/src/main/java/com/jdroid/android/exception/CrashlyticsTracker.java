package com.jdroid.android.exception;

import java.util.Map;
import java.util.Map.Entry;
import android.app.Activity;
import com.crashlytics.android.Crashlytics;
import com.jdroid.android.AbstractApplication;
import com.jdroid.android.analytics.AbstractAnalyticsTracker;
import com.jdroid.android.analytics.AppLoadingSource;
import com.jdroid.android.context.SecurityContext;

public class CrashlyticsTracker extends AbstractAnalyticsTracker {
	
	private static final CrashlyticsTracker INSTANCE = new CrashlyticsTracker();
	
	public static CrashlyticsTracker get() {
		return INSTANCE;
	}
	
	/**
	 * @see com.jdroid.android.analytics.AnalyticsTracker#isEnabled()
	 */
	@Override
	public Boolean isEnabled() {
		return AbstractApplication.get().getAppContext().isCrashlyticsEnabled();
	}
	
	/**
	 * @see com.jdroid.android.analytics.AbstractAnalyticsTracker#onInitExceptionHandler(java.util.Map)
	 */
	@Override
	public void onInitExceptionHandler(Map<String, String> metadata) {
		Crashlytics.getInstance().setDebugMode(AbstractApplication.get().getAppContext().isCrashlyticsDebugEnabled());
		Crashlytics.start(AbstractApplication.get());
		
		if (metadata != null) {
			for (Entry<String, String> entry : metadata.entrySet()) {
				if (entry.getValue() != null) {
					Crashlytics.setString(entry.getKey(), entry.getValue());
				}
			}
		}
	}
	
	/**
	 * @see com.jdroid.android.analytics.AbstractAnalyticsTracker#trackHandledException(java.lang.Throwable)
	 */
	@Override
	public void trackHandledException(Throwable throwable) {
		Crashlytics.logException(throwable);
	}
	
	/**
	 * @see com.jdroid.android.analytics.AbstractAnalyticsTracker#onActivityStart(android.app.Activity,
	 *      com.jdroid.android.analytics.AppLoadingSource, java.lang.Object)
	 */
	@Override
	public void onActivityStart(Activity activity, AppLoadingSource appLoadingSource, Object data) {
		if (appLoadingSource != null) {
			Crashlytics.setString(AppLoadingSource.class.getSimpleName(), appLoadingSource.getName());
		}
		
		Crashlytics.setString("UserId",
			SecurityContext.get().isAuthenticated() ? SecurityContext.get().getUser().getId().toString() : null);
		Crashlytics.log("Started " + activity.getClass().getSimpleName());
	}
}