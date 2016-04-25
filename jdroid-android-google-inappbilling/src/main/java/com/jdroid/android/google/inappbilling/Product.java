package com.jdroid.android.google.inappbilling;

import com.jdroid.android.google.inappbilling.Purchase.PurchaseState;

import org.json.JSONException;

public class Product {
	
	public enum ItemType {
		MANAGED("inapp"),
		SUBSCRIPTION("subs");
		
		private String type;
		
		ItemType(String type) {
			this.type = type;
		}
		
		public String getType() {
			return type;
		}
		
	}
	
	private ProductType productType;
	private String title;
	private String description;
	private Purchase purchase;
	private Boolean available;
	
	private String formattedPrice;
	private Double price;
	private String currencyCode;
	
	public Product(ProductType productType, String formattedPrice, Double price, String currencyCode, String title,
			String description) {
		this.productType = productType;
		this.title = title;
		this.description = description;
		this.formattedPrice = formattedPrice;
		this.price = price;
		this.currencyCode = currencyCode;
		available = true;
	}
	
	/**
	 * @return the Formatted price of the item, including its currency sign. The price does not include tax.
	 */
	public String getFormattedPrice() {
		return formattedPrice;
	}
	
	public Double getPrice() {
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
	
	public void setPurchase(String signatureBase64, String jsonPurchaseInfo, String signature) throws JSONException {
		available = false;
		purchase = new Purchase(jsonPurchaseInfo, signature);
		if (Security.verifyPurchase(signatureBase64, jsonPurchaseInfo, signature)) {
			if (!verifyDeveloperPayload()) {
				throw InAppBillingErrorCode.VERIFICATION_FAILED.newErrorCodeException("Purchase developer payload verification FAILED. "
						+ jsonPurchaseInfo);
			}
		} else {
			throw InAppBillingErrorCode.VERIFICATION_FAILED.newErrorCodeException("Purchase signature verification FAILED. "
					+ jsonPurchaseInfo);
		}
		
		purchase.markAsVerified();
	}
	
	public Boolean isPurchaseVerified() {
		return (purchase != null) && (purchase.getState() == PurchaseState.PURCHASED) && purchase.isVerified();
	}
	
	public Boolean isAvailable() {
		return available;
	}
	
	public void consume() {
		purchase = null;
		available = true;
	}
	
	public Boolean isWaitingToConsume() {
		return productType.isConsumable() && !available && isPurchaseVerified();
	}
	
	public String getDeveloperPayload() {
		/*
		 * TODO: for security, generate your payload here for verification. See the comments on verifyDeveloperPayload()
		 * for more info. Since this is a SAMPLE, we just use an empty string, but on a production app you should
		 * carefully generate this.
		 */
		return productType.getProductId();
	}
	
	/**
	 * Verifies the developer payload of a purchase.
	 * 
	 * @return
	 */
	protected Boolean verifyDeveloperPayload() {
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
		
		return purchase.getProductId().equals(payload);
	}
	
	/**
	 * @return the purchase
	 */
	public Purchase getPurchase() {
		return purchase;
	}
	
	public String getCurrencyCode() {
		return currencyCode;
	}
	
	public String getId() {
		return getProductType().getProductId();
	}
	
	@Override
	public String toString() {
		return "Product [productType=" + productType + ", title=" + title + ", description=" + description
				+ ", purchase=" + purchase + ", available=" + available + ", formattedPrice=" + formattedPrice
				+ ", price=" + price + ", currencyCode=" + currencyCode + "]";
	}
}
