package com.jdroid.android.firebase.analytics;

import android.app.Activity;
import android.os.Bundle;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.jdroid.android.analytics.AnalyticsTracker;
import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.social.AccountType;
import com.jdroid.android.social.SocialAction;
import com.jdroid.android.usecase.AbstractUseCase;
import com.jdroid.android.utils.DeviceUtils;
import com.jdroid.android.utils.SharedPreferencesHelper;

import java.util.List;
import java.util.Map;

public class FirebaseAnalyticsTracker extends AbstractFirebaseAnalyticsTracker implements AnalyticsTracker {

	private static final String INSTALLATION_SOURCE_USER_PROPERTY = "InstallationSource";
	private static final String DEVICE_YEAR_CLASS_USER_PROPERTY = "DeviceYearClass";

	private Boolean firstTrackingSent = false;

	@Override
	public void onInitExceptionHandler(Map<String, String> metadata) {
		// Do nothing
	}

	@Override
	public void trackFatalException(Throwable throwable, List<String> tags) {
		// Do nothing
	}

	@Override
	public void trackHandledException(Throwable throwable, List<String> tags) {
		// Do nothing
	}

	@Override
	public void trackErrorBreadcrumb(String message) {
		// Do nothing
	}

	@Override
	public void onActivityStart(Class<? extends Activity> activityClass, String referrer, Object data) {
		if (firstTrackingSent) {
			getFirebaseAnalyticsHelper().setUserProperty(DEVICE_YEAR_CLASS_USER_PROPERTY, DeviceUtils.getDeviceYearClass().toString());
			String installationSource = SharedPreferencesHelper.get().loadPreference(AbstractApplication.INSTALLATION_SOURCE);
			if (installationSource != null) {
				getFirebaseAnalyticsHelper().setUserProperty(INSTALLATION_SOURCE_USER_PROPERTY, installationSource);
			}
		}
	}

	@Override
	public void onActivityResume(Activity activity) {
		// Do nothing
	}

	@Override
	public void onActivityPause(Activity activity) {
		// Do nothing
	}

	@Override
	public void onActivityStop(Activity activity) {
		// Do nothing
	}

	@Override
	public void onActivityDestroy(Activity activity) {
		// Do nothing
	}

	@Override
	public void onFragmentStart(String screenViewName) {
		// Do nothing
	}

	@Override
	public void trackNotificationDisplayed(String notificationName) {
		Bundle bundle = new Bundle();
		bundle.putString("notificationName", notificationName);
		getFirebaseAnalyticsHelper().sendEvent("DisplayNotification", bundle);
	}

	@Override
	public void trackNotificationOpened(String notificationName) {
		Bundle bundle = new Bundle();
		bundle.putString("notificationName", notificationName);
		getFirebaseAnalyticsHelper().sendEvent("OpenNotification", bundle);
	}

	@Override
	public void trackEnjoyingApp(Boolean enjoying) {
		Bundle bundle = new Bundle();
		bundle.putString("enjoying", enjoying.toString());
		getFirebaseAnalyticsHelper().sendEvent("EnjoyingApp", bundle);
	}

	@Override
	public void trackRateOnGooglePlay(Boolean rate) {
		Bundle bundle = new Bundle();
		bundle.putString("rate", rate.toString());
		getFirebaseAnalyticsHelper().sendEvent("RateOnGooglePlay", bundle);
	}

	@Override
	public void trackGiveFeedback(Boolean feedback) {
		Bundle bundle = new Bundle();
		bundle.putString("feedback", feedback.toString());
		getFirebaseAnalyticsHelper().sendEvent("GiveFeedback", bundle);
	}

	@Override
	public void trackWidgetAdded(String widgetName) {
		// TODO
	}

	@Override
	public void trackWidgetRemoved(String widgetName) {
		// TODO
	}

	@Override
	public void trackUriOpened(String screenName, String referrer) {
		// TODO
	}

	@Override
	public void trackUseCaseTiming(Class<? extends AbstractUseCase> useCaseClass, long executionTime) {
		Bundle bundle = new Bundle();
		bundle.putString("useCase", useCaseClass.getSimpleName());
		bundle.putLong(FirebaseAnalytics.Param.VALUE, executionTime);
		getFirebaseAnalyticsHelper().sendEvent("ExecuteUseCase", bundle);
	}

	@Override
	public void trackServiceTiming(String trackingVariable, String trackingLabel, long executionTime) {
		Bundle bundle = new Bundle();
		bundle.putString("service", trackingVariable);
		bundle.putString("label", trackingLabel);
		bundle.putLong(FirebaseAnalytics.Param.VALUE, executionTime);
		getFirebaseAnalyticsHelper().sendEvent("ExecuteService", bundle);
	}

	@Override
	public void trackSocialInteraction(AccountType accountType, SocialAction socialAction, String socialTarget) {
		Bundle bundle = new Bundle();
		bundle.putString("accountType", accountType.getFriendlyName());
		bundle.putString("socialTarget", socialTarget);
		getFirebaseAnalyticsHelper().sendEvent(socialAction.getName(), bundle);
	}
}
