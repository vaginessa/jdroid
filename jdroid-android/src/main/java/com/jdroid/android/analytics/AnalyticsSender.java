package com.jdroid.android.analytics;

import java.util.List;
import org.slf4j.Logger;
import android.app.Activity;
import com.jdroid.java.collections.Lists;
import com.jdroid.java.utils.ExecutorUtils;
import com.jdroid.java.utils.LoggerUtils;

/**
 * 
 * @author Maxi Rosson
 */
public class AnalyticsSender implements AnalyticsTracker {
	
	private static final Logger LOGGER = LoggerUtils.getLogger(AnalyticsSender.class);
	
	private List<? extends AnalyticsTracker> trackers;
	
	private static final AnalyticsSender INSTANCE = new AnalyticsSender();
	
	private AnalyticsSender() {
		trackers = Lists.newArrayList(GoogleAnalyticsTracker.get());
	}
	
	public static AnalyticsSender get() {
		return INSTANCE;
	}
	
	private abstract class TrackerRunnable implements Runnable {
		
		@Override
		public void run() {
			try {
				for (AnalyticsTracker tracker : trackers) {
					if (tracker.isEnabled()) {
						track(tracker);
					}
				}
			} catch (Exception e) {
				LOGGER.error("Error tracking", e);
				// TODO We should log on Crittercism as a handled exception
			}
		}
		
		protected abstract void track(AnalyticsTracker tracker);
	}
	
	/**
	 * @see com.jdroid.android.analytics.AnalyticsTracker#onActivityStart(android.app.Activity)
	 */
	@Override
	public void onActivityStart(final Activity activity) {
		ExecutorUtils.execute(new TrackerRunnable() {
			
			@Override
			protected void track(AnalyticsTracker tracker) {
				tracker.onActivityStart(activity);
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
			protected void track(AnalyticsTracker tracker) {
				tracker.onActivityStop(activity);
			}
		});
	}
	
	/**
	 * @see com.jdroid.android.analytics.AnalyticsTracker#isEnabled()
	 */
	@Override
	public Boolean isEnabled() {
		return null;
	}
	
}
