package com.jdroid.android.inappbilling;

import android.preference.PreferenceManager;
import com.jdroid.android.AbstractApplication;
import com.jdroid.java.utils.PropertiesUtils;

public class BillingContext {
	
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
	
	public String getGooglePlayPublicKey() {
		return googlePlayPublicKey;
	}
	
	public Boolean isInAppBillingMockEnabled() {
		return !AbstractApplication.get().getAppContext().isProductionEnvironment()
				&& PreferenceManager.getDefaultSharedPreferences(AbstractApplication.get()).getBoolean(
					"inAppBillingMockEnabled", false);
	}
	
	public TestProductType getTestProductType() {
		return TestProductType.valueOf(PreferenceManager.getDefaultSharedPreferences(AbstractApplication.get()).getString(
			"inAppBillingTestProductIds", TestProductType.PURCHASED.name()));
	}
	
}
