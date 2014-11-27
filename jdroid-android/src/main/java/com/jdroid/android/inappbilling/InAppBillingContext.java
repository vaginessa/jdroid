package com.jdroid.android.inappbilling;

import java.util.List;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import com.jdroid.android.AbstractApplication;
import com.jdroid.java.collections.Lists;
import com.jdroid.java.utils.PropertiesUtils;
import com.jdroid.java.utils.StringUtils;

public class InAppBillingContext {
	
	public static final String MOCK_ENABLED = "inAppBillingMockEnabled";
	public static final String TEST_PRODUCT_IDS = "inAppBillingTestProductIds";
	public static final String PURCHASED_PRODUCT_TYPES = "inAppBillingPurchasedProductTypes";
	
	private String googlePlayPublicKey;
	private List<ProductType> purchasedProductTypes;
	
	private static final InAppBillingContext INSTANCE = new InAppBillingContext();
	
	/**
	 * @return The {@link InAppBillingContext} instance
	 */
	public static InAppBillingContext get() {
		return INSTANCE;
	}
	
	private InAppBillingContext() {
		
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
				&& PreferenceManager.getDefaultSharedPreferences(AbstractApplication.get()).getBoolean(MOCK_ENABLED,
					false);
	}
	
	public TestProductType getTestProductType() {
		return TestProductType.valueOf(PreferenceManager.getDefaultSharedPreferences(AbstractApplication.get()).getString(
			TEST_PRODUCT_IDS, TestProductType.PURCHASED.name()));
	}
	
	public synchronized void setPurchasedProductTypes(Inventory inventory) {
		purchasedProductTypes = Lists.newArrayList();
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(AbstractApplication.get());
		Editor editor = sharedPreferences.edit();
		List<String> productIds = Lists.newArrayList();
		for (Product each : inventory.getProducts()) {
			if (each.isPurchaseVerified()) {
				productIds.add(each.getId());
				purchasedProductTypes.add(each.getProductType());
			}
		}
		editor.putString(PURCHASED_PRODUCT_TYPES, StringUtils.join(productIds));
		editor.commit();
	}
	
	public synchronized void addPurchasedProductType(ProductType productType) {
		if (purchasedProductTypes != null) {
			purchasedProductTypes.add(productType);
		}
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(AbstractApplication.get());
		String currentValue = sharedPreferences.getString(PURCHASED_PRODUCT_TYPES, null);
		String newValue = productType.getProductId();
		Editor editor = sharedPreferences.edit();
		if (StringUtils.isNotEmpty(currentValue)) {
			newValue = "," + productType.getProductId();
		} else {
			newValue = productType.getProductId();
		}
		editor.putString(PURCHASED_PRODUCT_TYPES, newValue);
		editor.commit();
	}
	
	public synchronized List<ProductType> getPurchasedProductTypes() {
		if (purchasedProductTypes == null) {
			String pref = PreferenceManager.getDefaultSharedPreferences(AbstractApplication.get()).getString(
				PURCHASED_PRODUCT_TYPES, null);
			purchasedProductTypes = Lists.newArrayList();
			for (String each : StringUtils.splitToCollection(pref)) {
				ProductType productType = null;
				List<ProductType> supportedProductTypes = Lists.newArrayList();
				supportedProductTypes.addAll(AbstractApplication.get().getManagedProductTypes());
				supportedProductTypes.addAll(AbstractApplication.get().getSubscriptionsProductTypes());
				for (ProductType supportedProductType : supportedProductTypes) {
					if (supportedProductType.getProductId().equals(each)) {
						productType = supportedProductType;
						purchasedProductTypes.add(productType);
						break;
					}
				}
			}
		}
		return purchasedProductTypes;
	}
}
