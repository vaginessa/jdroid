package com.jdroid.android.inappbilling;

import android.preference.PreferenceManager;
import com.jdroid.android.AbstractApplication;
import com.jdroid.java.utils.PropertiesUtils;

public class BillingContext {
	
	public static final String IN_APP_BILLING_MOCK_ENABLED = "inAppBillingMockEnabled";
	public static final String IN_APP_BILLING_TEST_PRODUCT_IDS = "inAppBillingTestProductIds";
	
	private String googlePlayPublicKey;
	
	private static final BillingContext INSTANCE = new BillingContext();
	
	/**
	 * @return The {@link BillingContext} instance
	 */
	public static BillingContext get() {
		return INSTANCE;
	}
	
	private BillingContext() {
		
		// TODO Instead of just storing the entire literal string here embedded in the program, construct the key at
		// runtime from pieces or use bit manipulation (for example, XOR with some other string) to hide the actual key.
		// The key itself is not secret information, but we don't want to make it easy for an attacker to replace the
		// public key with one of their own and then fake messages from the server.
		googlePlayPublicKey = PropertiesUtils.getStringProperty("google.play.public.key");
		
	}
	
	/**
	 * @return Your application's public key, encoded in base64. This is used for verification of purchase signatures.
	 *         You can find your app's base64-encoded public key in your application's page on Google Play Developer
	 *         Console. Note that this is NOT your "developer public key".
	 */
	public String getGooglePlayPublicKey() {
		return googlePlayPublicKey;
	}
	
	public Boolean isInAppBillingMockEnabled() {
		return !AbstractApplication.get().getAppContext().isProductionEnvironment()
				&& PreferenceManager.getDefaultSharedPreferences(AbstractApplication.get()).getBoolean(
					IN_APP_BILLING_MOCK_ENABLED, false);
	}
	
	public TestProductType getTestProductType() {
		return TestProductType.valueOf(PreferenceManager.getDefaultSharedPreferences(AbstractApplication.get()).getString(
			IN_APP_BILLING_TEST_PRODUCT_IDS, TestProductType.PURCHASED.name()));
	}
	
}
