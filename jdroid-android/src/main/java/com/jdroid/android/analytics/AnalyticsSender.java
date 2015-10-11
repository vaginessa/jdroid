package com.jdroid.android.analytics;

import android.app.Activity;

import com.jdroid.android.google.inappbilling.Product;
import com.jdroid.android.social.AccountType;
import com.jdroid.android.social.SocialAction;
import com.jdroid.java.analytics.BaseAnalyticsSender;
import com.jdroid.java.concurrent.ExecutorUtils;
import com.jdroid.java.utils.LoggerUtils;

import org.slf4j.Logger;

import java.util.List;
import java.util.Map;

/**
 * 
 * @param <T>
 */
public class AnalyticsSender<T extends AnalyticsTracker> extends BaseAnalyticsSender<T> implements AnalyticsTracker {
	
	private static final Logger LOGGER = LoggerUtils.getLogger(AnalyticsSender.class);
	
	@SafeVarargs
	public AnalyticsSender(T... trackers) {
		super(trackers);
	}
	
	public AnalyticsSender(List<T> trackers) {
		super(trackers);
	}
	
	/**
	 * @see com.jdroid.android.analytics.AnalyticsTracker#onInitExceptionHandler(java.util.Map)
	 */
	@Override
	public void onInitExceptionHandler(Map<String, String> metadata) {
		try {
			for (T tracker : getTrackers()) {
				if (tracker.isEnabled()) {
					tracker.onInitExceptionHandler(metadata);
				}
			}
		} catch (Exception e) {
			LOGGER.error("Error when initializing the exception handler", e);
		}
	}
	
	/**
	 * @see com.jdroid.android.analytics.AnalyticsTracker#onActivityStart(java.lang.Class,
	 *      com.jdroid.android.analytics.AppLoadingSource, java.lang.Object)
	 */
	@Override
	public void onActivityStart(final Class<? extends Activity> activityClass, final AppLoadingSource appLoadingSource,
			final Object data) {
		ExecutorUtils.execute(new TrackerRunnable() {
			
			@Override
			protected void track(T tracker) {
				tracker.onActivityStart(activityClass, appLoadingSource, data);
			}
		});
	}
	
	/**
	 * @see com.jdroid.android.analytics.AnalyticsTracker#onActivityResume(android.app.Activity)
	 */
	@Override
	public void onActivityResume(final Activity activity) {
		ExecutorUtils.execute(new TrackerRunnable() {
			
			@Override
			protected void track(T tracker) {
				tracker.onActivityResume(activity);
			}
		});
	}
	
	/**
	 * @see com.jdroid.android.analytics.AnalyticsTracker#onActivityPause(android.app.Activity)
	 */
	@Override
	public void onActivityPause(final Activity activity) {
		ExecutorUtils.execute(new TrackerRunnable() {
			
			@Override
			protected void track(T tracker) {
				tracker.onActivityPause(activity);
			}
		});
	}
	
	/**
	 * @see com.jdroid.android.analytics.AnalyticsTracker#onActivityStop(android.app.Activity)
	 */
	@Override
	public void onActivityStop(final Activity activity) {
		ExecutorUtils.execute(new TrackerRunnable() {
			
			@Override
			protected void track(T tracker) {
				tracker.onActivityStop(activity);
			}
		});
	}
	
	/**
	 * @see com.jdroid.android.analytics.AnalyticsTracker#onActivityDestroy(android.app.Activity)
	 */
	@Override
	public void onActivityDestroy(final Activity activity) {
		ExecutorUtils.execute(new TrackerRunnable() {
			
			@Override
			protected void track(T tracker) {
				tracker.onActivityDestroy(activity);
			}
		});
	}
	
	/**
	 * @see com.jdroid.android.analytics.AnalyticsTracker#onFragmentStart(java.lang.String)
	 */
	@Override
	public void onFragmentStart(final String screenViewName) {
		ExecutorUtils.execute(new TrackerRunnable() {
			
			@Override
			protected void track(T tracker) {
				tracker.onFragmentStart(screenViewName);
			}
		});
	}

	@Override
	public void trackFatalException(final Throwable throwable) {
		ExecutorUtils.execute(new Runnable() {
			@Override
			public void run() {
				for (T tracker : getTrackers()) {
					if (tracker.isEnabled()) {
						tracker.trackFatalException(throwable);
					}
				}
			}
		});
	}
	
	@Override
	public void trackHandledException(final Throwable throwable, final int priority) {
		ExecutorUtils.execute(new Runnable() {
			@Override
			public void run() {
				for (T tracker : getTrackers()) {
					try {
						if (tracker.isEnabled()) {
							tracker.trackHandledException(throwable, priority);
						}
					} catch (Exception e) {
						LOGGER.error("Error when trying to track the exception.", e);
					}
				}
			}
		});
	}
	
	/**
	 * @see com.jdroid.android.analytics.AnalyticsTracker#trackUriOpened(java.lang.String, java.lang.String)
	 */
	@Override
	public void trackUriOpened(final String uriType, final String screenName) {
		ExecutorUtils.execute(new TrackerRunnable() {
			
			@Override
			protected void track(T tracker) {
				tracker.trackUriOpened(uriType, screenName);
			}
		});
	}
	
	/**
	 * @see com.jdroid.android.analytics.AnalyticsTracker#trackInAppBillingPurchaseTry(com.jdroid.android.google.inappbilling.Product)
	 */
	@Override
	public void trackInAppBillingPurchaseTry(final Product product) {
		ExecutorUtils.execute(new TrackerRunnable() {
			
			@Override
			protected void track(T tracker) {
				tracker.trackInAppBillingPurchaseTry(product);
			}
		});
	}
	
	/**
	 * @see com.jdroid.android.analytics.AnalyticsTracker#trackInAppBillingPurchase(com.jdroid.android.google.inappbilling.Product)
	 */
	@Override
	public void trackInAppBillingPurchase(final Product product) {
		ExecutorUtils.execute(new TrackerRunnable() {
			
			@Override
			protected void track(T tracker) {
				tracker.trackInAppBillingPurchase(product);
			}
		});
	}
	
	/**
	 * @see com.jdroid.android.analytics.AnalyticsTracker#trackSocialInteraction(com.jdroid.android.social.AccountType,
	 *      com.jdroid.android.social.SocialAction, java.lang.String)
	 */
	@Override
	public void trackSocialInteraction(final AccountType accountType, final SocialAction socialAction,
			final String socialTarget) {
		ExecutorUtils.execute(new TrackerRunnable() {
			
			@Override
			protected void track(T tracker) {
				tracker.trackSocialInteraction(accountType, socialAction, socialTarget);
			}
		});
	}
	
	/**
	 * @see com.jdroid.android.analytics.AnalyticsTracker#trackNotificationDisplayed(java.lang.String)
	 */
	@Override
	public void trackNotificationDisplayed(final String notificationName) {
		ExecutorUtils.execute(new TrackerRunnable() {
			
			@Override
			protected void track(T tracker) {
				tracker.trackNotificationDisplayed(notificationName);
			}
		});
	}
	
	/**
	 * @see com.jdroid.android.analytics.AnalyticsTracker#trackNotificationOpened(java.lang.String)
	 */
	@Override
	public void trackNotificationOpened(final String notificationName) {
		ExecutorUtils.execute(new TrackerRunnable() {
			
			@Override
			protected void track(T tracker) {
				tracker.trackNotificationOpened(notificationName);
			}
		});
	}
	
	/**
	 * @see com.jdroid.android.analytics.AnalyticsTracker#trackRemoveAdsBannerClicked()
	 */
	@Override
	public void trackRemoveAdsBannerClicked() {
		ExecutorUtils.execute(new TrackerRunnable() {
			
			@Override
			protected void track(T tracker) {
				tracker.trackRemoveAdsBannerClicked();
			}
		});
	}
	
	/**
	 * @see com.jdroid.android.analytics.AnalyticsTracker#trackRateMeBannerClicked()
	 */
	@Override
	public void trackRateMeBannerClicked() {
		ExecutorUtils.execute(new TrackerRunnable() {
			
			@Override
			protected void track(T tracker) {
				tracker.trackRateMeBannerClicked();
			}
		});
	}
	
	/**
	 * @see com.jdroid.android.analytics.AnalyticsTracker#trackTiming(java.lang.String, java.lang.String,
	 *      java.lang.String, long)
	 */
	@Override
	public void trackTiming(final String category, final String variable, final String label, final long value) {
		ExecutorUtils.execute(new TrackerRunnable() {
			
			@Override
			protected void track(T tracker) {
				tracker.trackTiming(category, variable, label, value);
			}
		});
	}
	
	/**
	 * @see com.jdroid.android.analytics.AnalyticsTracker#trackRateUs()
	 */
	@Override
	public void trackRateUs() {
		ExecutorUtils.execute(new TrackerRunnable() {
			
			@Override
			protected void track(T tracker) {
				tracker.trackRateUs();
			}
		});
	}
	
	/**
	 * @see com.jdroid.android.analytics.AnalyticsTracker#trackContactUs()
	 */
	@Override
	public void trackContactUs() {
		ExecutorUtils.execute(new TrackerRunnable() {
			
			@Override
			protected void track(T tracker) {
				tracker.trackContactUs();
			}
		});
	}
	
	/**
	 * @see com.jdroid.android.analytics.AnalyticsTracker#trackAboutLibraryOpen(java.lang.String)
	 */
	@Override
	public void trackAboutLibraryOpen(final String libraryKey) {
		ExecutorUtils.execute(new TrackerRunnable() {
			
			@Override
			protected void track(T tracker) {
				tracker.trackAboutLibraryOpen(libraryKey);
			}
		});
	}
	
}
