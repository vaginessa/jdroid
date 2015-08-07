package com.jdroid.android.analytics;

import android.app.Activity;

import com.jdroid.android.google.inappbilling.Product;
import com.jdroid.android.social.AccountType;
import com.jdroid.android.social.SocialAction;

import java.util.Map;

public interface AnalyticsTracker {
	
	public Boolean isEnabled();
	
	public void onInitExceptionHandler(Map<String, String> metadata);
	
	public void onActivityStart(Class<? extends Activity> activityClass, AppLoadingSource appLoadingSource, Object data);
	
	public void onActivityResume(Activity activity);
	
	public void onActivityPause(Activity activity);
	
	public void onActivityStop(Activity activity);
	
	public void onActivityDestroy(Activity activity);
	
	public void onFragmentStart(String screenViewName);
	
	public void trackFatalException(Throwable throwable);

	public void trackHandledException(Throwable throwable);

	public void trackUriOpened(String uriType, String screenName);
	
	public void trackInAppBillingPurchaseTry(Product product);
	
	public void trackInAppBillingPurchase(Product product);
	
	public void trackSocialInteraction(AccountType accountType, SocialAction socialAction, String socialTarget);
	
	public void trackNotificationDisplayed(String notificationName);
	
	public void trackNotificationOpened(String notificationName);
	
	public void trackRemoveAdsBannerClicked();
	
	public void trackRateMeBannerClicked();
	
	public void trackTiming(String category, String variable, String label, long value);
	
	public void trackRateUs();
	
	public void trackContactUs();
	
	public void trackAboutLibraryOpen(String libraryKey);
	
}
