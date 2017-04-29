package com.jdroid.android.sample.ui.google.inappbilling;


import com.jdroid.android.google.inappbilling.client.Product;
import com.jdroid.android.google.inappbilling.client.ProductType;

public enum SampleProductType implements ProductType {
	
	REMOVE_ADS("remove.ads", false, null, null);
	
	private String productId;
	private Boolean consumable;
	private Integer titleId;
	private Integer descriptionId;
	
	private SampleProductType(String productId, Boolean consumable, Integer titleId, Integer descriptionId) {
		this.productId = productId;
		this.consumable = consumable;
		this.titleId = titleId;
		this.descriptionId = descriptionId;
	}
	
	@Override
	public String getProductId() {
		return productId;
	}
	
	@Override
	public Integer getTitleId() {
		return titleId;
	}
	
	@Override
	public Integer getDescriptionId() {
		return descriptionId;
	}
	
	@Override
	public Boolean isConsumable() {
		return consumable;
	}
	
	@Override
	public Product.ItemType getItemType() {
		return Product.ItemType.MANAGED;
	}
	
}
