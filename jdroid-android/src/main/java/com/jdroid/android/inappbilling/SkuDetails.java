package com.jdroid.android.inappbilling;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Represents an in-app product's listing details.
 */
public class SkuDetails {
	
	private String mSku;
	private String mItemType;
	private String mTitle;
	private String mDescription;
	
	private String formattedPrice;
	private Double price;
	private String currencyCode;
	
	public SkuDetails(String jsonSkuDetails) throws JSONException {
		JSONObject o = new JSONObject(jsonSkuDetails);
		mSku = o.optString("productId");
		mItemType = o.optString("type");
		mTitle = o.optString("title");
		mDescription = o.optString("description");
		
		formattedPrice = o.optString("price");
		price = o.optDouble("price_amount_micros") / 1000000;
		currencyCode = o.optString("price_currency_code");
		
	}
	
	public String getSku() {
		return mSku;
	}
	
	public String getItemType() {
		return mItemType;
	}
	
	public String getFormattedPrice() {
		return formattedPrice;
	}
	
	public String getTitle() {
		return mTitle;
	}
	
	public String getDescription() {
		return mDescription;
	}
	
	/**
	 * @return the price
	 */
	public Double getPrice() {
		return price;
	}
	
	/**
	 * @return the currencyCode
	 */
	public String getCurrencyCode() {
		return currencyCode;
	}
	
	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "SkuDetails [mItemType=" + mItemType + ", mSku=" + mSku + ", formattedPrice=" + formattedPrice
				+ ", mTitle=" + mTitle + ", mDescription=" + mDescription + ", price=" + price + ", currencyCode="
				+ currencyCode + "]";
	}
	
}
