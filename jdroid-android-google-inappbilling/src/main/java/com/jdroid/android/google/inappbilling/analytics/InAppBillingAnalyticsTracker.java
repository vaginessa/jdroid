package com.jdroid.android.google.inappbilling.analytics;

import com.jdroid.android.google.inappbilling.Product;
import com.jdroid.java.analytics.BaseAnalyticsTracker;

public interface InAppBillingAnalyticsTracker extends BaseAnalyticsTracker {

	public void trackInAppBillingPurchaseTry(Product product);

	public void trackInAppBillingPurchase(Product product);

}
