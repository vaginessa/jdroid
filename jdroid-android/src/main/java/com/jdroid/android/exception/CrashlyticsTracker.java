package com.jdroid.android.exception;

import android.app.Activity;

import com.crashlytics.android.Crashlytics;
import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.analytics.AbstractAnalyticsTracker;
import com.jdroid.android.analytics.AppLoadingSource;
import com.jdroid.android.context.SecurityContext;

import java.util.Map;
import java.util.Map.Entry;

import io.fabric.sdk.android.Fabric;

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
		Fabric.with(AbstractApplication.get(), new Crashlytics());

		if (metadata != null) {
			for (Entry<String, String> entry : metadata.entrySet()) {
				if (entry.getValue() != null) {
					Crashlytics.getInstance().core.setString(entry.getKey(), entry.getValue());
				}
			}
		}
	}
	
	/**
	 * @see com.jdroid.android.analytics.AbstractAnalyticsTracker#trackHandledException(java.lang.Throwable)
	 */
	@Override
	public void trackHandledException(Throwable throwable) {
		Crashlytics.getInstance().core.logException(throwable);
	}
	
	/**
	 * @see com.jdroid.android.analytics.AbstractAnalyticsTracker#onActivityStart(java.lang.Class,
	 *      com.jdroid.android.analytics.AppLoadingSource, java.lang.Object)
	 */
	@Override
	public void onActivityStart(Class<? extends Activity> activityClass, AppLoadingSource appLoadingSource, Object data) {
		if (appLoadingSource != null) {
			Crashlytics.getInstance().core.setString(AppLoadingSource.class.getSimpleName(), appLoadingSource.getName());
		}
		
		Crashlytics.getInstance().core.setString("UserId",
				SecurityContext.get().isAuthenticated() ? SecurityContext.get().getUser().getId().toString() : null);

		Crashlytics.getInstance().core.log("Started " + activityClass.getSimpleName());
	}
}