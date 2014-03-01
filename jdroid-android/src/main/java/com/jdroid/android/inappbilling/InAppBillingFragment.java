package com.jdroid.android.inappbilling;

import java.util.List;
import org.slf4j.Logger;
import android.content.Intent;
import android.os.Bundle;
import com.jdroid.android.exception.CommonErrorCode;
import com.jdroid.android.fragment.AbstractFragment;
import com.jdroid.android.inappbilling.InAppBillingClient.OnConsumeFinishedListener;
import com.jdroid.android.inappbilling.InAppBillingClient.OnIabPurchaseFinishedListener;
import com.jdroid.android.inappbilling.InAppBillingClient.QueryInventoryFinishedListener;
import com.jdroid.java.collections.Lists;
import com.jdroid.java.concurrent.ExecutorUtils;
import com.jdroid.java.utils.LoggerUtils;

/**
 * 
 * @author Maxi Rosson
 */
public abstract class InAppBillingFragment extends AbstractFragment {
	
	private static final Logger LOGGER = LoggerUtils.getLogger(InAppBillingFragment.class);
	
	public static final int PURCHASE_REQUEST_CODE = 10001;
	
	private InAppBillingClient inAppBillingClient;
	private List<Product> products;
	
	/**
	 * @see com.jdroid.android.activity.AbstractActivity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Create the client, passing it our context and the public key to verify signatures with
		inAppBillingClient = new InAppBillingClient(getActivity(), BillingContext.get().getGooglePlayPublicKey());
		
		// Start setup. This is asynchronous and the specified listener will be called once setup completes.
		inAppBillingClient.startSetup(new InAppBillingClient.OnIabSetupFinishedListener() {
			
			@Override
			public void onIabSetupFinished(InAppBillingResponseCode result) {
				
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
					
				} else if (result == InAppBillingResponseCode.BILLING_UNAVAILABLE) {
					onNotSupportedInAppBilling();
				} else {
					LOGGER.warn("Problem setting up in-app billing: " + result);
				}
			}
		});
	}
	
	protected abstract List<? extends ProductType> getProductTypes();
	
	protected abstract ProductType getProductType(String productId);
	
	// Listener that's called when we finish querying the items and subscriptions we own
	private QueryInventoryFinishedListener queryInventoryListener = new InAppBillingClient.QueryInventoryFinishedListener() {
		
		@Override
		public void onQueryInventoryFinished(InAppBillingResponseCode result, Inventory inventory) {
			
			// Have we been disposed of in the meantime? If so, quit.
			if (inAppBillingClient == null) {
				return;
			}
			
			if (result.isSuccess()) {
				LOGGER.debug("Query inventory was successful.");
				
				List<Product> productsToConsume = Lists.newArrayList();
				
				products = Lists.newArrayList();
				for (ProductType each : getProductTypes()) {
					SkuDetails skuDetails = inventory.getSkuDetails(each.getProductId());
					if (skuDetails != null) {
						ProductType productType = BillingContext.get().isInAppBillingMockEnabled() ? BillingContext.get().getTestProductType()
								: each;
						Product product = new Product(productType, skuDetails.getPrice(), getString(each.getTitleId()),
								getString(each.getDescriptionId(), skuDetails.getPrice()), each.getLayoutId());
						product.setPurchase(inventory.getPurchase(product.getProductType().getProductId()));
						products.add(product);
						
						if (product.isPurchaseVerified() && product.getProductType().isConsumable()) {
							productsToConsume.add(product);
						}
					}
				}
				onProductsLoaded(products);
				
				for (Product each : productsToConsume) {
					inAppBillingClient.consumeAsync(each.getPurchase(), consumeListener);
				}
				
			} else {
				LOGGER.warn("Failed to query inventory: " + result);
				onFailedToLoadPurchases();
			}
		}
	};
	
	protected void onNotSupportedInAppBilling() {
		ExecutorUtils.execute(new Runnable() {
			
			@Override
			public void run() {
				throw CommonErrorCode.INAPP_BILLING_NOT_SUPPORTED.newApplicationException("Not supported in app billing");
			}
		});
	}
	
	protected void onFailedToLoadPurchases() {
		ExecutorUtils.execute(new Runnable() {
			
			@Override
			public void run() {
				throw CommonErrorCode.INAPP_BILLING_FAILED_TO_LOAD_PURCHASES.newApplicationException("Failed to query inventory");
			}
		});
	}
	
	protected abstract void onProductsLoaded(List<Product> products);
	
	protected abstract void onPurchased(Product product);
	
	protected abstract void onConsumed(Product product);
	
	public void launchPurchaseFlow(Product product) {
		inAppBillingClient.launchPurchaseFlow(getActivity(), product.getProductType().getProductId(),
			PURCHASE_REQUEST_CODE, purchaseListener, product.generatePayload());
	}
	
	// Callback for when a purchase is finished
	private OnIabPurchaseFinishedListener purchaseListener = new InAppBillingClient.OnIabPurchaseFinishedListener() {
		
		@Override
		public void onIabPurchaseFinished(InAppBillingResponseCode result, Purchase purchase) {
			LOGGER.info("Purchase finished: " + result + ", purchase: " + purchase);
			
			// if we were disposed of in the meantime, quit.
			if (inAppBillingClient == null) {
				return;
			}
			
			if (result.isSuccess()) {
				
				Product product = findProduct(purchase);
				
				if (!product.verifyDeveloperPayload(purchase)) {
					LOGGER.warn("Authenticity verification failed " + result);
					return;
				}
				product.setPurchase(purchase);
				
				onPurchased(product);
				
				if (product.getProductType().isConsumable()) {
					inAppBillingClient.consumeAsync(purchase, consumeListener);
				}
			} else {
				LOGGER.warn("Failed to purchase item: " + result);
			}
			
		}
		
	};
	
	private Product findProduct(Purchase purchase) {
		Product product = null;
		for (Product each : products) {
			if (each.getProductType().getProductId().equals(purchase.getSku())) {
				product = each;
				break;
			}
		}
		return product;
	}
	
	// Called when consumption is complete
	private OnConsumeFinishedListener consumeListener = new InAppBillingClient.OnConsumeFinishedListener() {
		
		@Override
		public void onConsumeFinished(Purchase purchase, InAppBillingResponseCode result) {
			LOGGER.info("Consumption finished. Purchase: " + purchase + ", result: " + result);
			
			// if we were disposed of in the meantime, quit.
			if (inAppBillingClient == null) {
				return;
			}
			
			if (result.isSuccess()) {
				
				Product product = findProduct(purchase);
				product.setPurchase(null);
				
				onConsumed(product);
			} else {
				LOGGER.warn("Failed to consume item: " + result);
			}
		}
	};
	
	/**
	 * @see com.jdroid.android.activity.AbstractActivity#onActivityResult(int, int, android.content.Intent)
	 */
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
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
