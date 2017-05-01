package com.jdroid.android.google.inappbilling.client;

import com.jdroid.android.google.inappbilling.client.Product.ItemType;

public interface ProductType {
	
	/*
	 * @return The product ID for the product
	 */
	public String getProductId();
	
	/*
	 * @return The product ID for the product used when the static responses is enabled
	 */
	public String getTestProductId();
	
	/*
	 * Non-consumable Items. Once purchased, these items will be permanently associated to the user's Google account. An example of a non-consumable in-app product is a premium upgrade or a level pack.
	 * Consumable items. Items that can be made available for purchase multiple times.
	 */
	public Boolean isConsumable();
	
	public ItemType getItemType();
	
	public Integer getTitleId();
	
	public Integer getDescriptionId();
	
}
