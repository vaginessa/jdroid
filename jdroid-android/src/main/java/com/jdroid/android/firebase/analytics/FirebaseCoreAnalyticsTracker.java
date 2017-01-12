package com.jdroid.android.firebase.analytics;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.jdroid.android.analytics.CoreAnalyticsTracker;
import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.social.AccountType;
import com.jdroid.android.social.SocialAction;
import com.jdroid.android.usecase.AbstractUseCase;
import com.jdroid.android.utils.DeviceUtils;
import com.jdroid.android.utils.ScreenUtils;

import java.util.List;

public class FirebaseCoreAnalyticsTracker extends AbstractFirebaseAnalyticsTracker implements CoreAnalyticsTracker {

	private static final String INSTALLATION_SOURCE_USER_PROPERTY = "INSTALLATION_SOURCE";
	private static final String DEVICE_YEAR_CLASS_USER_PROPERTY = "DEVICE_YEAR_CLASS";
	private static final String SCREEN_WIDTH = "SCREEN_WIDTH";
	private static final String SCREEN_HEIGHT = "SCREEN_HEIGHT";
	private static final String SCREEN_DENSITY = "SCREEN_DENSITY";
	private static final String SCREEN_DENSITY_DPI = "SCREEN_DENSITY_DPI";

	private Boolean firstTrackingSent = false;

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
	public void onActivityCreate(Activity activity, Bundle savedInstanceState) {
		// Do nothing
	}

	@Override
	public void onActivityStart(Activity activity, String referrer, Object data) {
		if (!firstTrackingSent) {
			getFirebaseAnalyticsHelper().setUserProperty(DEVICE_YEAR_CLASS_USER_PROPERTY, DeviceUtils.getDeviceYearClass().toString());
			getFirebaseAnalyticsHelper().setUserProperty(SCREEN_WIDTH, ScreenUtils.getScreenWidthDp().toString());
			getFirebaseAnalyticsHelper().setUserProperty(SCREEN_HEIGHT, ScreenUtils.getScreenHeightDp().toString());
			getFirebaseAnalyticsHelper().setUserProperty(SCREEN_DENSITY, ScreenUtils.getScreenDensity());
			getFirebaseAnalyticsHelper().setUserProperty(SCREEN_DENSITY_DPI, ScreenUtils.getDensityDpi().toString());
			getFirebaseAnalyticsHelper().setUserProperty(INSTALLATION_SOURCE_USER_PROPERTY, AbstractApplication.get().getInstallationSource());
			firstTrackingSent = true;
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
		Bundle bundle = new Bundle();
		bundle.putString("widgetName", widgetName);
		getFirebaseAnalyticsHelper().sendEvent("AddWidget", bundle);
	}

	@Override
	public void trackWidgetRemoved(String widgetName) {
		Bundle bundle = new Bundle();
		bundle.putString("widgetName", widgetName);
		getFirebaseAnalyticsHelper().sendEvent("RemoveWidget", bundle);
	}

	@Override
	public void trackUriOpened(String screenName, Uri uri, String referrer) {
		Bundle bundle = new Bundle();
		bundle.putString("screenName", screenName);
		bundle.putString("referrer", referrer);
		getFirebaseAnalyticsHelper().sendEvent("OpenUri", bundle);
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
		if (accountType != null) {
			bundle.putString("accountType", accountType.getFriendlyName());
		}
		bundle.putString("socialTarget", socialTarget);
		getFirebaseAnalyticsHelper().sendEvent(socialAction.getName(), bundle);
	}

	@Override
	public void trackSendAppInvitation(String invitationId) {
		Bundle bundle = new Bundle();
		bundle.putString(FirebaseAnalytics.Param.ITEM_ID, invitationId);
		getFirebaseAnalyticsHelper().sendEvent("sendAppInvitation", bundle);
	}
}
