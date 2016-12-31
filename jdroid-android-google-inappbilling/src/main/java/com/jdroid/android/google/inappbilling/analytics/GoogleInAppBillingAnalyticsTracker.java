package com.jdroid.android.google.inappbilling.analytics;

import com.google.android.gms.analytics.HitBuilders;
import com.jdroid.android.google.analytics.AbstractGoogleAnalyticsTracker;
import com.jdroid.android.google.inappbilling.Product;

public class GoogleInAppBillingAnalyticsTracker extends AbstractGoogleAnalyticsTracker implements InAppBillingAnalyticsTracker {

	@Override
	public void trackInAppBillingPurchaseTry(Product product) {
		getGoogleAnalyticsHelper().sendEvent("inAppBilling", "purchaseTry", product.getProductType().getProductId());
	}

	@Override
	public void trackInAppBillingPurchase(Product product) {
		HitBuilders.TransactionBuilder transactionBuilder = new HitBuilders.TransactionBuilder();
		transactionBuilder.setTransactionId(product.getPurchase().getOrderId());
		transactionBuilder.setAffiliation("Google Play");
		transactionBuilder.setRevenue(product.getPrice());
		transactionBuilder.setCurrencyCode(product.getCurrencyCode());
		getGoogleAnalyticsHelper().sendTransaction(transactionBuilder);

		HitBuilders.ItemBuilder itemBuilder = new HitBuilders.ItemBuilder();
		itemBuilder.setTransactionId(product.getPurchase().getOrderId());
		itemBuilder.setName(product.getProductType().getProductId());
		itemBuilder.setCategory(product.getProductType().isConsumable() ? "consumable" : "notConsumable");
		itemBuilder.setSku(product.getProductType().getProductId());
		itemBuilder.setQuantity(1);
		itemBuilder.setPrice(product.getPrice());
		itemBuilder.setCurrencyCode(product.getCurrencyCode());
		getGoogleAnalyticsHelper().sendTransactionItem(itemBuilder);
	}
}
