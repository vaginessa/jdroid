package com.jdroid.android.inappbilling;

import com.jdroid.android.inappbilling.Purchase.PurchaseState;
import com.jdroid.java.utils.Hasher;

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
	private Purchase purchase;
	private Boolean consumed;
	
	public Product(ProductType productType, String price, String title, String description, int layoutId) {
		this.productType = productType;
		this.price = price;
		this.title = title;
		this.description = description;
		this.layoutId = layoutId;
		consumed = false;
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
	
	public void setPurchase(Purchase purchase) {
		this.purchase = purchase;
		consumed = isPurchaseVerified();
	}
	
	public void setConsumed(Boolean consumed) {
		this.consumed = consumed;
	}
	
	public Boolean isPurchaseVerified() {
		return (purchase != null) && (purchase.getState() == PurchaseState.PURCHASED)
				&& verifyDeveloperPayload(purchase);
	}
	
	public Boolean isAvailable() {
		return productType.isConsumable() || !consumed;
	}
	
	public Boolean isConsumed() {
		return consumed;
	}
	
	protected String generatePayload() {
		/*
		 * TODO: for security, generate your payload here for verification. See the comments on verifyDeveloperPayload()
		 * for more info. Since this is a SAMPLE, we just use an empty string, but on a production app you should
		 * carefully generate this.
		 */
		return Hasher.SHA_512.hash(productType.getProductId());
	}
	
	/**
	 * Verifies the developer payload of a purchase.
	 * 
	 * @param purchase
	 * @return
	 */
	protected Boolean verifyDeveloperPayload(Purchase purchase) {
		String payload = purchase.getDeveloperPayload();
		
		/*
		 * TODO: verify that the developer payload of the purchase is correct. It will be the same one that you sent
		 * when initiating the purchase. WARNING: Locally generating a random string when starting a purchase and
		 * verifying it here might seem like a good approach, but this will fail in the case where the user purchases an
		 * item on one device and then uses your app on a different device, because on the other device you will not
		 * have access to the random string you originally generated. So a good developer payload has these
		 * characteristics: 1. If two different users purchase an item, the payload is different between them, so that
		 * one user's purchase can't be replayed to another user. 2. The payload must be such that you can verify it
		 * even when the app wasn't the one who initiated the purchase flow (so that items purchased by the user on one
		 * device work on other devices owned by the user). Using your own server to store and verify developer payloads
		 * across app installations is recommended.
		 */
		
		return Hasher.SHA_512.hash(purchase.getSku()).equals(payload);
	}
	
	/**
	 * @return the purchase
	 */
	public Purchase getPurchase() {
		return purchase;
	}
}
