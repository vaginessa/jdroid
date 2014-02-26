/*
 * Copyright (c) 2012 Google Inc. Licensed under the Apache License, Version 2.0 (the "License"); you may not use this
 * file except in compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language governing permissions and limitations under the
 * License.
 */

package com.jdroid.android.inappbilling.sample;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Represents an in-app billing purchase.
 */
public class Purchase {
	
	public enum PurchaseState {
		PURCHASED(0),
		CANCELED(1),
		REFUNDED(2);
		
		private int code;
		
		private PurchaseState(int code) {
			this.code = code;
		}
		
		public static PurchaseState valueOf(int code) {
			PurchaseState state = PurchaseState.PURCHASED;
			for (PurchaseState each : values()) {
				if (each.code == code) {
					return each;
				}
			}
			return state;
		}
	}
	
	private String mItemType; // ITEM_TYPE_INAPP or ITEM_TYPE_SUBS
	private String mOrderId;
	private String mPackageName;
	private String mSku;
	private long mPurchaseTime;
	private PurchaseState state;
	private String mDeveloperPayload;
	private String mToken;
	private String mSignature;
	
	public Purchase(String itemType, String jsonPurchaseInfo, String signature) throws JSONException {
		mItemType = itemType;
		JSONObject o = new JSONObject(jsonPurchaseInfo);
		mOrderId = o.optString("orderId");
		mPackageName = o.optString("packageName");
		mSku = o.optString("productId");
		mPurchaseTime = o.optLong("purchaseTime");
		state = PurchaseState.valueOf(o.optInt("purchaseState"));
		mDeveloperPayload = o.optString("developerPayload");
		mToken = o.optString("token", o.optString("purchaseToken"));
		mSignature = signature;
	}
	
	public String getItemType() {
		return mItemType;
	}
	
	public String getOrderId() {
		return mOrderId;
	}
	
	public String getPackageName() {
		return mPackageName;
	}
	
	public String getSku() {
		return mSku;
	}
	
	public long getPurchaseTime() {
		return mPurchaseTime;
	}
	
	public PurchaseState getState() {
		return state;
	}
	
	public String getDeveloperPayload() {
		return mDeveloperPayload;
	}
	
	public String getToken() {
		return mToken;
	}
	
	public String getSignature() {
		return mSignature;
	}
	
	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Purchase [mItemType=" + mItemType + ", mOrderId=" + mOrderId + ", mPackageName=" + mPackageName
				+ ", mSku=" + mSku + ", mPurchaseTime=" + mPurchaseTime + ", state=" + state + ", mDeveloperPayload="
				+ mDeveloperPayload + ", mToken=" + mToken + ", mSignature=" + mSignature + "]";
	}
	
}
