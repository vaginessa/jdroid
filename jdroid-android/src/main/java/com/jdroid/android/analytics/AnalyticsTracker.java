package com.jdroid.android.analytics;

import java.util.Map;
import android.app.Activity;
import com.jdroid.android.inappbilling.Product;
import com.jdroid.android.social.AccountType;
import com.jdroid.android.social.SocialAction;
import com.jdroid.java.exception.ConnectionException;

/**
 * 
 * @author Maxi Rosson
 */
public interface AnalyticsTracker {
	
	public Boolean isEnabled();
	
	public void onInitExceptionHandler(Map<String, String> metadata);
	
	public void onActivityStart(Activity activity, AppLoadingSource appLoadingSource, Object data);
	
	public void onActivityResume(Activity activity);
	
	public void onActivityPause(Activity activity);
	
	public void onActivityStop(Activity activity);
	
	public void onActivityDestroy(Activity activity);
	
	public void trackConnectionException(ConnectionException connectionException);
	
	public void trackHandledException(Throwable throwable);
	
	public void trackUriHandled(Boolean handled, String validUri, String invalidUri);
	
	public void trackInAppBillingPurchaseTry(Product product);
	
	public void trackInAppBillingPurchase(Product product);
	
	public void trackSocialInteraction(AccountType accountType, SocialAction socialAction, String socialTarget);
	
	public void trackNotificationDisplayed(String notificationName);
	
	public void trackNotificationOpened(String notificationName);
	
	public void trackRemoveAdsBannerClicked();
	
	public void trackRateMeBannerClicked();
	
}
