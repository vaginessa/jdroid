package com.jdroid.android.google.inappbilling;

import com.jdroid.android.google.inappbilling.Product.ItemType;

public interface ProductType {
	
	public String getProductId();
	
	public Boolean isConsumable();
	
	public ItemType getItemType();
	
	public Integer getTitleId();
	
	public Integer getDescriptionId();
	
}
