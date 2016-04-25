/*
 * Copyright (c) 2012 Google Inc. Licensed under the Apache License, Version 2.0 (the "License"); you may not use this
 * file except in compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language governing permissions and limitations under the
 * License.
 */

package com.jdroid.android.google.inappbilling;

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
		
		PurchaseState(int code) {
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
	
	private String orderId;
	private String productId;
	private PurchaseState state;
	private String developerPayload;
	private String token;
	private String signature;
	private Boolean verified;
	
	public Purchase(String jsonPurchaseInfo, String signature) throws JSONException {
		JSONObject jsonObject = new JSONObject(jsonPurchaseInfo);
		orderId = jsonObject.optString("orderId");
		productId = jsonObject.optString("productId");
		state = PurchaseState.valueOf(jsonObject.optInt("purchaseState"));
		developerPayload = jsonObject.optString("developerPayload");
		token = jsonObject.optString("token", jsonObject.optString("purchaseToken"));
		this.signature = signature;
		verified = false;
	}
	
	public String getOrderId() {
		return orderId;
	}
	
	public String getProductId() {
		return productId;
	}
	
	public PurchaseState getState() {
		return state;
	}
	
	public String getDeveloperPayload() {
		return developerPayload;
	}
	
	public String getToken() {
		return token;
	}
	
	public String getSignature() {
		return signature;
	}
	
	public void markAsVerified() {
		verified = true;
	}
	
	public Boolean isVerified() {
		return verified;
	}
	
	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Purchase [orderId=" + orderId + ", productId=" + productId + ", state=" + state + ", developerPayload="
				+ developerPayload + ", token=" + token + ", signature=" + signature + "]";
	}
	
}
