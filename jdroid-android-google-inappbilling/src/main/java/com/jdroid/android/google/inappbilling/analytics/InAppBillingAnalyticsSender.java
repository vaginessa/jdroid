package com.jdroid.android.google.inappbilling.analytics;

import com.jdroid.android.google.inappbilling.Product;
import com.jdroid.java.analytics.AnalyticsSender;
import com.jdroid.java.concurrent.ExecutorUtils;

import java.util.List;

public class InAppBillingAnalyticsSender extends AnalyticsSender<InAppBillingAnalyticsTracker> implements InAppBillingAnalyticsTracker {

	public InAppBillingAnalyticsSender(List<InAppBillingAnalyticsTracker> trackers) {
		super(trackers);
	}

	@Override
	public void trackInAppBillingPurchaseTry(final Product product) {
		ExecutorUtils.execute(new TrackerRunnable() {

			@Override
			protected void track(InAppBillingAnalyticsTracker tracker) {
				tracker.trackInAppBillingPurchaseTry(product);
			}
		});
	}

	@Override
	public void trackInAppBillingPurchase(final Product product) {
		ExecutorUtils.execute(new TrackerRunnable() {

			@Override
			protected void track(InAppBillingAnalyticsTracker tracker) {
				tracker.trackInAppBillingPurchase(product);
			}
		});
	}
}
