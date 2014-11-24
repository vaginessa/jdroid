package com.jdroid.android.inappbilling;

import com.jdroid.android.inappbilling.Product.ItemType;

public interface ProductType {
	
	public String getProductId();
	
	public Boolean isConsumable();
	
	public ItemType getItemType();
	
	public Integer getTitleId();
	
	public Integer getDescriptionId();
	
}
