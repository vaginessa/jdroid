package com.jdroid.android.google.firebase.analytics;

import android.app.Activity;
import android.os.Bundle;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.jdroid.android.analytics.AbstractAnalyticsTracker;
import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.google.firebase.FirebaseAppModule;
import com.jdroid.android.social.AccountType;
import com.jdroid.android.social.SocialAction;
import com.jdroid.android.usecase.AbstractUseCase;
import com.jdroid.android.utils.DeviceUtils;
import com.jdroid.android.utils.SharedPreferencesHelper;

public class FirebaseAnalyticsTracker extends AbstractAnalyticsTracker {

	private static final String INSTALLATION_SOURCE_USER_PROPERTY = "InstallationSource";
	private static final String DEVICE_YEAR_CLASS_USER_PROPERTY = "DeviceYearClass";

	private FirebaseAnalyticsHelper firebaseAnalyticsHelper;
	private Boolean firstTrackingSent = false;

	public FirebaseAnalyticsTracker() {
		firebaseAnalyticsHelper = FirebaseAppModule.get().getFirebaseAnalyticsHelper();
	}

	@Override
	public Boolean isEnabled() {
		return FirebaseAppModule.get().getFirebaseAppContext().isFirebaseAnalyticsEnabled();
	}

	@Override
	public void onActivityStart(Class<? extends Activity> activityClass, String referrer, Object data) {
		if (firstTrackingSent) {
			firebaseAnalyticsHelper.setUserProperty(DEVICE_YEAR_CLASS_USER_PROPERTY, DeviceUtils.getDeviceYearClass().toString());
			String installationSource = SharedPreferencesHelper.get().loadPreference(AbstractApplication.INSTALLATION_SOURCE);
			if (installationSource != null) {
				firebaseAnalyticsHelper.setUserProperty(INSTALLATION_SOURCE_USER_PROPERTY, installationSource);
			}
		}
	}

	@Override
	public void trackNotificationDisplayed(String notificationName) {
		Bundle bundle = new Bundle();
		bundle.putString("notificationName", notificationName);
		firebaseAnalyticsHelper.sendEvent("DisplayNotification", bundle);
	}

	@Override
	public void trackNotificationOpened(String notificationName) {
		Bundle bundle = new Bundle();
		bundle.putString("notificationName", notificationName);
		firebaseAnalyticsHelper.sendEvent("OpenNotification", bundle);
	}

	@Override
	public void trackEnjoyingApp(Boolean enjoying) {
		Bundle bundle = new Bundle();
		bundle.putString("enjoying", enjoying.toString());
		firebaseAnalyticsHelper.sendEvent("EnjoyingApp", bundle);
	}

	@Override
	public void trackRateOnGooglePlay(Boolean rate) {
		Bundle bundle = new Bundle();
		bundle.putString("rate", rate.toString());
		firebaseAnalyticsHelper.sendEvent("RateOnGooglePlay", bundle);
	}

	@Override
	public void trackGiveFeedback(Boolean feedback) {
		Bundle bundle = new Bundle();
		bundle.putString("feedback", feedback.toString());
		firebaseAnalyticsHelper.sendEvent("GiveFeedback", bundle);
	}

	@Override
	public void trackUseCaseTiming(Class<? extends AbstractUseCase> useCaseClass, long executionTime) {
		Bundle bundle = new Bundle();
		bundle.putString("useCase", useCaseClass.getSimpleName());
		bundle.putLong(FirebaseAnalytics.Param.VALUE, executionTime);
		firebaseAnalyticsHelper.sendEvent("ExecuteUseCase", bundle);
	}

	@Override
	public void trackServiceTiming(String trackingVariable, String trackingLabel, long executionTime) {
		Bundle bundle = new Bundle();
		bundle.putString("service", trackingVariable);
		bundle.putString("label", trackingLabel);
		bundle.putLong(FirebaseAnalytics.Param.VALUE, executionTime);
		firebaseAnalyticsHelper.sendEvent("ExecuteService", bundle);
	}

	@Override
	public void trackSocialInteraction(AccountType accountType, SocialAction socialAction, String socialTarget) {
		Bundle bundle = new Bundle();
		bundle.putString("accountType", accountType.getFriendlyName());
		bundle.putString("socialTarget", socialTarget);
		firebaseAnalyticsHelper.sendEvent(socialAction.getName(), bundle);
	}

	public FirebaseAnalyticsHelper getFirebaseAnalyticsHelper() {
		return firebaseAnalyticsHelper;
	}
}
