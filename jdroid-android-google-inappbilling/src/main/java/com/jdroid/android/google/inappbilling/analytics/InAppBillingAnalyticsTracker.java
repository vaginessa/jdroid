package com.jdroid.android.google.inappbilling.analytics;

import com.jdroid.android.google.inappbilling.Product;
import com.jdroid.java.analytics.AnalyticsTracker;

public interface InAppBillingAnalyticsTracker extends AnalyticsTracker {

	public void trackInAppBillingPurchaseTry(Product product);

	public void trackInAppBillingPurchase(Product product);

}
