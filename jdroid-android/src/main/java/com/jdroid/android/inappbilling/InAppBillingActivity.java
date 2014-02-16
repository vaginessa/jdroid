package com.jdroid.android.inappbilling;

import java.util.List;
import org.slf4j.Logger;
import android.content.Intent;
import android.os.Bundle;
import com.jdroid.android.activity.AbstractActivity;
import com.jdroid.android.inappbilling.sample.IabResult;
import com.jdroid.android.inappbilling.sample.InAppBillingClient;
import com.jdroid.android.inappbilling.sample.InAppBillingClient.OnConsumeFinishedListener;
import com.jdroid.android.inappbilling.sample.InAppBillingClient.OnIabPurchaseFinishedListener;
import com.jdroid.android.inappbilling.sample.InAppBillingClient.QueryInventoryFinishedListener;
import com.jdroid.android.inappbilling.sample.Inventory;
import com.jdroid.android.inappbilling.sample.Purchase;
import com.jdroid.android.inappbilling.sample.SkuDetails;
import com.jdroid.java.collections.Lists;
import com.jdroid.java.utils.Hasher;
import com.jdroid.java.utils.LoggerUtils;

/**
 * 
 * @author Maxi Rosson
 */
public abstract class InAppBillingActivity extends AbstractActivity {
	
	private static final Logger LOGGER = LoggerUtils.getLogger(InAppBillingActivity.class);
	
	public static final int PURCHASE_REQUEST_CODE = 10001;
	
	private InAppBillingClient inAppBillingClient;
	
	/**
	 * @see com.jdroid.android.activity.AbstractActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Create the client, passing it our context and the public key to verify signatures with
		inAppBillingClient = new InAppBillingClient(this, BillingContext.get().getGooglePlayPublicKey());
		inAppBillingClient.enableDebugLogging(LoggerUtils.isEnabled());
		
		// Start setup. This is asynchronous and the specified listener will be called once setup completes.
		inAppBillingClient.startSetup(new InAppBillingClient.OnIabSetupFinishedListener() {
			
			@Override
			public void onIabSetupFinished(IabResult result) {
				
				// Have we been disposed of in the meantime? If so, quit.
				if (inAppBillingClient == null) {
					return;
				}
				
				if (result.isSuccess()) {
					
					// IAB is fully set up. Now, let's get an inventory of stuff we own.
					LOGGER.info("Setup successful. Querying inventory.");
					
					List<String> productsIds = Lists.newArrayList();
					for (ProductType each : getProductTypes()) {
						productsIds.add(each.getProductId());
					}
					inAppBillingClient.queryInventoryAsync(true, productsIds, queryInventoryListener);
					
				} else {
					LOGGER.warn("Problem setting up in-app billing: " + result);
				}
			}
		});
	}
	
	protected abstract List<? extends ProductType> getProductTypes();
	
	private List<String> getConsumableProductIds() {
		List<String> consumableProductIds = Lists.newArrayList();
		for (ProductType productType : getProductTypes()) {
			if (productType.isConsumable()) {
				consumableProductIds.add(productType.getProductId());
			}
		}
		return consumableProductIds;
	}
	
	protected abstract ProductType getProductType(String productId);
	
	// Listener that's called when we finish querying the items and subscriptions we own
	private QueryInventoryFinishedListener queryInventoryListener = new InAppBillingClient.QueryInventoryFinishedListener() {
		
		@Override
		public void onQueryInventoryFinished(IabResult result, Inventory inventory) {
			
			// Have we been disposed of in the meantime? If so, quit.
			if (inAppBillingClient == null) {
				return;
			}
			
			if (result.isSuccess()) {
				LOGGER.debug("Query inventory was successful.");
				
				List<Product> availableProducts = Lists.newArrayList();
				for (ProductType each : getProductTypes()) {
					SkuDetails skuDetails = inventory.getSkuDetails(each.getProductId());
					if (skuDetails != null) {
						
						ProductType productType = BillingContext.get().isInAppBillingMockEnabled() ? BillingContext.get().getTestProductType()
								: each;
						availableProducts.add(new Product(productType, skuDetails.getPrice(),
								getString(each.getTitleId()), getString(each.getDescriptionId()), each.getLayoutId()));
					}
				}
				onAvailableProducts(availableProducts);
				
				List<Purchase> verifiedPurchases = Lists.newArrayList();
				for (Purchase each : inventory.getAllPurchases()) {
					if (verifyDeveloperPayload(each)) {
						verifiedPurchases.add(each);
					}
				}
				onPurchases(verifiedPurchases);
				
				List<String> consumableProductIds = getConsumableProductIds();
				for (Purchase purchase : verifiedPurchases) {
					if (consumableProductIds.contains(purchase.getSku())) {
						inAppBillingClient.consumeAsync(purchase, consumeListener);
					}
				}
				
			} else {
				LOGGER.warn("Failed to query inventory: " + result);
			}
		}
	};
	
	protected abstract void onAvailableProducts(List<Product> availableProducts);
	
	protected abstract void onPurchases(List<Purchase> purchases);
	
	protected abstract void onPurchased(Purchase purchase);
	
	protected abstract void onConsumed(Purchase purchase);
	
	public void launchPurchaseFlow(Product product) {
		
		String payload = generatePayload(product);
		
		inAppBillingClient.launchPurchaseFlow(InAppBillingActivity.this, product.getProductType().getProductId(),
			PURCHASE_REQUEST_CODE, purchaseListener, payload);
	}
	
	protected String generatePayload(Product product) {
		/*
		 * TODO: for security, generate your payload here for verification. See the comments on verifyDeveloperPayload()
		 * for more info. Since this is a SAMPLE, we just use an empty string, but on a production app you should
		 * carefully generate this.
		 */
		return Hasher.SHA_512.hash(product.getProductType().getProductId());
	}
	
	/**
	 * Verifies the developer payload of a purchase.
	 * 
	 * @param purchase
	 * @return
	 */
	protected boolean verifyDeveloperPayload(Purchase purchase) {
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
	
	// Callback for when a purchase is finished
	private OnIabPurchaseFinishedListener purchaseListener = new InAppBillingClient.OnIabPurchaseFinishedListener() {
		
		@Override
		public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
			LOGGER.info("Purchase finished: " + result + ", purchase: " + purchase);
			
			// if we were disposed of in the meantime, quit.
			if (inAppBillingClient == null) {
				return;
			}
			
			if (result.isSuccess()) {
				
				if (!verifyDeveloperPayload(purchase)) {
					LOGGER.warn("Authenticity verification failed " + result);
					return;
				}
				
				onPurchased(purchase);
				
				if (getProductType(purchase.getSku()).isConsumable()) {
					inAppBillingClient.consumeAsync(purchase, consumeListener);
				}
			} else {
				LOGGER.warn("Failed to purchase item: " + result);
			}
			
		}
	};
	
	// Called when consumption is complete
	private OnConsumeFinishedListener consumeListener = new InAppBillingClient.OnConsumeFinishedListener() {
		
		@Override
		public void onConsumeFinished(Purchase purchase, IabResult result) {
			LOGGER.info("Consumption finished. Purchase: " + purchase + ", result: " + result);
			
			// if we were disposed of in the meantime, quit.
			if (inAppBillingClient == null) {
				return;
			}
			
			if (result.isSuccess()) {
				onConsumed(purchase);
			} else {
				LOGGER.warn("Failed to consume item: " + result);
			}
		}
	};
	
	/**
	 * @see com.jdroid.android.activity.AbstractActivity#onActivityResult(int, int, android.content.Intent)
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (inAppBillingClient != null) {
			// Pass on the activity result to the helper for handling
			if (!inAppBillingClient.handleActivityResult(requestCode, resultCode, data)) {
				// not handled, so handle it ourselves (here's where you'd
				// perform any handling of activity results not related to in-app
				// billing...
				super.onActivityResult(requestCode, resultCode, data);
			}
		} else {
			super.onActivityResult(requestCode, resultCode, data);
		}
	}
	
	/**
	 * @see com.jdroid.android.activity.AbstractActivity#onDestroy()
	 */
	@Override
	public void onDestroy() {
		super.onDestroy();
		
		if (inAppBillingClient != null) {
			inAppBillingClient.dispose();
			inAppBillingClient = null;
		}
	}
	
}
