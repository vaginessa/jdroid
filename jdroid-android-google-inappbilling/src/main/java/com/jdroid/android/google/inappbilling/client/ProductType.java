package com.jdroid.android.google.inappbilling.client;

import com.jdroid.android.google.inappbilling.client.Product.ItemType;

public interface ProductType {
	
	public String getProductId();
	
	public Boolean isConsumable();
	
	public ItemType getItemType();
	
	public Integer getTitleId();
	
	public Integer getDescriptionId();
	
}
