package com.jdroid.android.analytics;

import android.app.Activity;

import com.jdroid.android.social.AccountType;
import com.jdroid.android.social.SocialAction;
import com.jdroid.android.usecase.AbstractUseCase;

import java.util.List;
import java.util.Map;

public abstract class AbstractCoreAnalyticsTracker implements CoreAnalyticsTracker {
	
	@Override
	public void onInitExceptionHandler(Map<String, String> metadata) {
		// Do Nothing
	}

	@Override
	public void trackErrorBreadcrumb(String message) {
		// Do Nothing
	}

	@Override
	public void onActivityCreate(Activity activity) {
		// Do nothing
	}

	@Override
	public void onActivityStart(Activity activity, String referrer, Object data) {
		// Do Nothing
	}
	
	@Override
	public void onActivityResume(Activity activity) {
		// Do Nothing
	}
	
	@Override
	public void onActivityPause(Activity activity) {
		// Do Nothing
	}
	
	@Override
	public void onActivityStop(Activity activity) {
		// Do Nothing
	}
	
	@Override
	public void onActivityDestroy(Activity activity) {
		// Do Nothing
	}
	
	@Override
	public void onFragmentStart(String screenViewName) {
		// Do Nothing
	}

	@Override
	public void trackFatalException(Throwable throwable, List<String> tags) {
		// Do Nothing
	}

	@Override
	public void trackHandledException(Throwable throwable, List<String> tags) {
		// Do Nothing
	}
	
	@Override
	public void trackUriOpened(String screenName, String referrer) {
		// Do Nothing
	}
	
	@Override
	public void trackSocialInteraction(AccountType accountType, SocialAction socialAction, String socialTarget) {
		// Do Nothing
	}
	
	@Override
	public void trackNotificationDisplayed(String notificationName) {
		// Do Nothing
	}
	
	@Override
	public void trackNotificationOpened(String notificationName) {
		// Do Nothing
	}

	@Override
	public void trackEnjoyingApp(Boolean enjoying) {
		// Do Nothing
	}

	@Override
	public void trackRateOnGooglePlay(Boolean rate) {
		// Do Nothing
	}

	@Override
	public void trackGiveFeedback(Boolean feedback) {
		// Do Nothing
	}

	@Override
	public void trackUseCaseTiming(Class<? extends  AbstractUseCase> useCaseClass, long executionTime) {
		// Do Nothing
	}

	@Override
	public void trackServiceTiming(String trackingVariable, String trackingLabel, long executionTime) {
		// Do Nothing
	}

	// Widgets

	public void trackWidgetAdded(String widgetName) {
		// Do Nothing
	}

	public void trackWidgetRemoved(String widgetName) {
		// Do Nothing
	}

	@Override
	public void trackSendAppInvitation(String invitationId) {
		// Do Nothing
	}
}