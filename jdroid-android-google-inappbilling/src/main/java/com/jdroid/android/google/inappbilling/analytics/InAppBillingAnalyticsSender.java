package com.jdroid.android.google.inappbilling.analytics;

import com.jdroid.android.google.inappbilling.Product;
import com.jdroid.java.analytics.AnalyticsSender;

import java.util.List;

public class InAppBillingAnalyticsSender extends AnalyticsSender<InAppBillingAnalyticsTracker> implements InAppBillingAnalyticsTracker {

	public InAppBillingAnalyticsSender(List<InAppBillingAnalyticsTracker> trackers) {
		super(trackers);
	}

	@Override
	public void trackInAppBillingPurchaseTry(final Product product) {
		execute(new TrackingCommand() {

			@Override
			protected void track(InAppBillingAnalyticsTracker tracker) {
				tracker.trackInAppBillingPurchaseTry(product);
			}
		});
	}

	@Override
	public void trackInAppBillingPurchase(final Product product) {
		execute(new TrackingCommand() {

			@Override
			protected void track(InAppBillingAnalyticsTracker tracker) {
				tracker.trackInAppBillingPurchase(product);
			}
		});
	}
}
