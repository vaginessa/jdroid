package com.jdroid.android.inappbilling;

/**
 * 
 * @author Maxi Rosson
 */
public interface ProductType {
	
	public String getProductId();
	
	public Boolean isConsumable();
	
	public Integer getLayoutId();
	
	public Integer getTitleId();
	
	public Integer getDescriptionId();
	
}
