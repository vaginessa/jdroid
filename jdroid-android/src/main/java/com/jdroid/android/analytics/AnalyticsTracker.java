package com.jdroid.android.analytics;

import android.app.Activity;

import com.jdroid.android.social.AccountType;
import com.jdroid.android.social.SocialAction;
import com.jdroid.android.usecase.AbstractUseCase;
import com.jdroid.java.analytics.BaseAnalyticsTracker;

import java.util.List;
import java.util.Map;

public interface AnalyticsTracker extends BaseAnalyticsTracker {

	// Error handling
	
	public void onInitExceptionHandler(Map<String, String> metadata);

	public void trackFatalException(Throwable throwable, List<String> tags);

	public void trackHandledException(Throwable throwable, List<String> tags);

	public void trackErrorBreadcrumb(String message);

	// Activity/fragment life cycle
	
	public void onActivityStart(Class<? extends Activity> activityClass, String referrer, Object data);
	
	public void onActivityResume(Activity activity);
	
	public void onActivityPause(Activity activity);
	
	public void onActivityStop(Activity activity);
	
	public void onActivityDestroy(Activity activity);
	
	public void onFragmentStart(String screenViewName);

	// Notifications

	public void trackNotificationDisplayed(String notificationName);

	public void trackNotificationOpened(String notificationName);

	// Feedback

	public void trackEnjoyingApp(Boolean enjoying);

	public void trackRateOnGooglePlay(Boolean rate);

	public void trackGiveFeedback(Boolean feedback);

	// Widgets

	public void trackWidgetAdded(String widgetName);

	public void trackWidgetRemoved(String widgetName);

	// More

	public void trackUriOpened(String referrerCategory, String screenName);

	public void trackSocialInteraction(AccountType accountType, SocialAction socialAction, String socialTarget);

	public void trackUseCaseTiming(Class<? extends AbstractUseCase> useCaseClass, long executionTime);

	public void trackServiceTiming(String trackingVariable, String trackingLabel, long executionTime);

}
