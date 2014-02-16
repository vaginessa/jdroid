package com.jdroid.android.inappbilling;

/**
 * 
 * @author Maxi Rosson
 */
public class Product {
	
	private ProductType productType;
	private String price;
	private String title;
	private String description;
	private int layoutId;
	
	public Product(ProductType productType, String price, String title, String description, int layoutId) {
		this.productType = productType;
		this.price = price;
		this.title = title;
		this.description = description;
		this.layoutId = layoutId;
	}
	
	/**
	 * @return the price
	 */
	public String getPrice() {
		return price;
	}
	
	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}
	
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	
	/**
	 * @return the productType
	 */
	public ProductType getProductType() {
		return productType;
	}
	
	/**
	 * @return the layoutId
	 */
	public int getLayoutId() {
		return layoutId;
	}
}
