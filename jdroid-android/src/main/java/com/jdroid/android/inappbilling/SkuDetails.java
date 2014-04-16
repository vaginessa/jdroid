/*
 * Copyright (c) 2012 Google Inc. Licensed under the Apache License, Version 2.0 (the "License"); you may not use this
 * file except in compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language governing permissions and limitations under the
 * License.
 */

package com.jdroid.android.inappbilling;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Represents an in-app product's listing details.
 */
public class SkuDetails {
	
	private String mItemType;
	private String mSku;
	private String mType;
	private String mTitle;
	private String mDescription;
	
	private String formattedPrice;
	private Double price;
	private String currencyCode;
	
	public SkuDetails(String itemType, String jsonSkuDetails) throws JSONException {
		mItemType = itemType;
		JSONObject o = new JSONObject(jsonSkuDetails);
		mSku = o.optString("productId");
		mType = o.optString("type");
		mTitle = o.optString("title");
		mDescription = o.optString("description");
		
		formattedPrice = o.optString("price");
		price = o.optDouble("price_amount_micros") / 1000000;
		currencyCode = o.optString("price_currency_code");
		
	}
	
	public String getSku() {
		return mSku;
	}
	
	public String getType() {
		return mType;
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
		return "SkuDetails [mItemType=" + mItemType + ", mSku=" + mSku + ", mType=" + mType + ", formattedPrice="
				+ formattedPrice + ", mTitle=" + mTitle + ", mDescription=" + mDescription + ", price=" + price
				+ ", currencyCode=" + currencyCode + "]";
	}
	
}
