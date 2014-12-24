package com.jdroid.android.inappbilling;

import java.util.List;

public interface InAppBillingListener {
	
	/**
	 * This method is executed (on the UI thread) when the products are loaded
	 * 
	 * @param products The loaded products
	 */
	public void onProductsLoaded(List<Product> products);
	
	/**
	 * This method is executed (on the UI thread) when the product was purchased
	 * 
	 * @param product The purchased {@link Product}
	 */
	public void onPurchased(Product product);
	
	/**
	 * This method is executed (on the UI thread) when the product was consumed
	 * 
	 * @param product The consumed {@link Product}
	 */
	public void onConsumed(Product product);
}
