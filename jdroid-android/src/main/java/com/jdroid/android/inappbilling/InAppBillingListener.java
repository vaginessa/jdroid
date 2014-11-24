package com.jdroid.android.inappbilling;

import java.util.List;

public interface InAppBillingListener {
	
	public List<ProductType> getManagedProductTypes();
	
	public List<ProductType> getSubscriptionsProductTypes();
	
	/**
	 * This method is executed (on the UI thread) when the products are loaded
	 * 
	 * @param products The loaded products
	 */
	public void onProductsLoaded(List<Product> products);
	
	public void onPurchased(Product product);
	
	public void onConsumed(Product product);
}
