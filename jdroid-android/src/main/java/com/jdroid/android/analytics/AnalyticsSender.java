package com.jdroid.android.analytics;

import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import android.app.Activity;
import android.support.v4.app.Fragment;
import com.jdroid.android.AbstractApplication;
import com.jdroid.android.exception.ExceptionHandler;
import com.jdroid.android.inappbilling.Product;
import com.jdroid.android.social.AccountType;
import com.jdroid.android.social.SocialAction;
import com.jdroid.java.collections.Lists;
import com.jdroid.java.concurrent.ExecutorUtils;
import com.jdroid.java.exception.ConnectionException;
import com.jdroid.java.utils.LoggerUtils;

/**
 * 
 * @param <T>
 */
public class AnalyticsSender<T extends AnalyticsTracker> implements AnalyticsTracker {
	
	private static final Logger LOGGER = LoggerUtils.getLogger(AnalyticsSender.class);
	
	private List<T> trackers = Lists.newArrayList();
	
	@SafeVarargs
	public AnalyticsSender(T... trackers) {
		this(Lists.newArrayList(trackers));
	}
	
	public AnalyticsSender(List<T> trackers) {
		for (T tracker : trackers) {
			if (tracker.isEnabled()) {
				this.trackers.add(tracker);
			}
		}
	}
	
	public abstract class TrackerRunnable implements Runnable {
		
		@Override
		public void run() {
			for (T tracker : trackers) {
				try {
					if (tracker.isEnabled()) {
						track(tracker);
					}
				} catch (Exception e) {
					ExceptionHandler exceptionHandler = AbstractApplication.get().getExceptionHandler();
					if (exceptionHandler != null) {
						exceptionHandler.logHandledException(e);
					}
				}
			}
		}
		
		protected abstract void track(T tracker);
	}
	
	/**
	 * @see com.jdroid.android.analytics.AnalyticsTracker#isEnabled()
	 */
	@Override
	public Boolean isEnabled() {
		return null;
	}
	
	/**
	 * @see com.jdroid.android.analytics.AnalyticsTracker#onInitExceptionHandler(java.util.Map)
	 */
	@Override
	public void onInitExceptionHandler(Map<String, String> metadata) {
		try {
			for (T tracker : trackers) {
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
	 * @see com.jdroid.android.analytics.AnalyticsTracker#onFragmentStart(android.support.v4.app.Fragment)
	 */
	@Override
	public void onFragmentStart(final Fragment fragment) {
		ExecutorUtils.execute(new TrackerRunnable() {
			
			@Override
			protected void track(T tracker) {
				tracker.onFragmentStart(fragment);
			}
		});
	}
	
	/**
	 * @see com.jdroid.android.analytics.AnalyticsTracker#trackConnectionException(com.jdroid.java.exception.ConnectionException)
	 */
	@Override
	public void trackConnectionException(final ConnectionException connectionException) {
		ExecutorUtils.execute(new TrackerRunnable() {
			
			@Override
			protected void track(T tracker) {
				tracker.trackConnectionException(connectionException);
			}
		});
	}
	
	/**
	 * @see com.jdroid.android.analytics.AnalyticsTracker#trackHandledException(java.lang.Throwable)
	 */
	@Override
	public void trackHandledException(final Throwable throwable) {
		for (T tracker : trackers) {
			if (tracker.isEnabled()) {
				tracker.trackHandledException(throwable);
			}
		}
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
	 * @see com.jdroid.android.analytics.AnalyticsTracker#trackInAppBillingPurchaseTry(com.jdroid.android.inappbilling.Product)
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
	 * @see com.jdroid.android.analytics.AnalyticsTracker#trackInAppBillingPurchase(com.jdroid.android.inappbilling.Product)
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
	
}
