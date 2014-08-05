package com.jdroid.android.inappbilling;

/**
 * The response codes for a request, defined by the Google Play app.
 */
public enum InAppBillingResponseCode {
	
	// Indicates that the request was sent to the server successfully. When this code is returned in response to a
	// CHECK_BILLING_SUPPORTED request, indicates that billing is supported.
	OK(0) {
		
		@Override
		public Boolean isSuccess() {
			return true;
		}
	},
	
	// Indicates that the user pressed the back button on the checkout dialog instead of buying the item.
	USER_CANCELED(1),
	
	// Indicates that in-app billing is not available because the API_VERSION that you specified is not recognized by
	// the Google Play app or the user is ineligible for in-app billing (for example, the user resides in a
	// country that prohibits in-app purchases).
	BILLING_UNAVAILABLE(3),
	
	// Indicates that the Google Play app cannot find the requested item in the application's product list. This can
	// happen
	// if the product ID is misspelled in your REQUEST_PURCHASE request or if an item is unpublished in the
	// application's product list.
	ITEM_UNAVAILABLE(4),
	
	// Indicates that an application is trying to make an in-app billing request but the application has not declared
	// the com.android.vending.BILLING permission in its manifest. Can also indicate that an application is not properly
	// signed, or that you sent a malformed request, such as a request with missing Bundle keys or a request that uses
	// an unrecognized request type.
	DEVELOPER_ERROR(5),
	
	// Indicates an unexpected server error. For example, this error is triggered if you try to purchase an item from
	// yourself, which is not allowed by Google Checkout.
	ERROR(6),
	
	// Failure to purchase since item is already owned
	ITEM_ALREADY_OWNED(7),
	
	// Failure to consume since item is not owned
	ITEM_NOT_OWNED(8),
	
	IABHELPER_ERROR_BASE(1000),
	
	// Remote Exception while setting up in-app billing
	IABHELPER_REMOTE_EXCEPTION(1001),
	
	// Bad response received
	IABHELPER_BAD_RESPONSE(1002),
	
	// Purchase signature verification failed
	IABHELPER_VERIFICATION_FAILED(1003),
	
	// Send intent failed
	IABHELPER_SEND_INTENT_FAILED(1004),
	
	// User cancelled
	IABHELPER_USER_CANCELLED(1005),
	
	// Unknown purchase response
	IABHELPER_UNKNOWN_PURCHASE_RESPONSE(1006),
	
	// Missing token
	IABHELPER_MISSING_TOKEN(1007),
	
	// Unknown error
	IABHELPER_UNKNOWN_ERROR(1008),
	
	// Subscriptions are not available.
	IABHELPER_SUBSCRIPTIONS_NOT_AVAILABLE(1009),
	
	// Invalid consumption attempt
	IABHELPER_INVALID_CONSUMPTION(1010);
	
	private int code;
	
	private InAppBillingResponseCode(int code) {
		this.code = code;
	}
	
	/**
	 * Converts from an ordinal value to the InAppBillingResponseCode
	 * 
	 * @param code
	 * @return The {@link InAppBillingResponseCode}
	 */
	public static InAppBillingResponseCode valueOf(int code) {
		InAppBillingResponseCode responseCode = InAppBillingResponseCode.ERROR;
		for (InAppBillingResponseCode each : values()) {
			if (each.code == code) {
				return each;
			}
		}
		return responseCode;
	}
	
	public Boolean isSuccess() {
		return false;
	}
}