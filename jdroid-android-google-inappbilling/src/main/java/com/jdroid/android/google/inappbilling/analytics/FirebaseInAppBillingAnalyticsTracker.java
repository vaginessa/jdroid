package com.jdroid.android.google.inappbilling.analytics;

import android.os.Bundle;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.jdroid.android.firebase.analytics.AbstractFirebaseAnalyticsTracker;
import com.jdroid.android.google.inappbilling.client.Product;

public class FirebaseInAppBillingAnalyticsTracker extends AbstractFirebaseAnalyticsTracker implements InAppBillingAnalyticsTracker {

	@Override
	public void trackInAppBillingPurchaseTry(Product product) {
		Bundle bundle = new Bundle();
		bundle.putString(FirebaseAnalytics.Param.ITEM_ID, product.getProductType().getProductId());
		getFirebaseAnalyticsHelper().sendEvent("in_app_purchase_try", bundle);
	}

	@Override
	public void trackInAppBillingPurchase(Product product) {
		// Tracked automatically by Firebase
	}
}
