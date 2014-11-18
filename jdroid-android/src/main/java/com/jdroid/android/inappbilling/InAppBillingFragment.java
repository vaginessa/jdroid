package com.jdroid.android.inappbilling;

import java.util.List;
import org.slf4j.Logger;
import android.content.Intent;
import android.os.Bundle;
import com.jdroid.android.AbstractApplication;
import com.jdroid.android.R;
import com.jdroid.android.exception.DefaultExceptionHandler;
import com.jdroid.android.fragment.AbstractGridFragment;
import com.jdroid.android.inappbilling.InAppBillingClient.OnConsumeFinishedListener;
import com.jdroid.android.inappbilling.InAppBillingClient.InAppBillingPurchaseFinishedListener;
import com.jdroid.android.inappbilling.InAppBillingClient.QueryInventoryFinishedListener;
import com.jdroid.android.loading.FragmentLoading;
import com.jdroid.android.loading.NonBlockingLoading;
import com.jdroid.java.collections.Lists;
import com.jdroid.java.concurrent.ExecutorUtils;
import com.jdroid.java.exception.ErrorCodeException;
import com.jdroid.java.exception.UnexpectedException;
import com.jdroid.java.utils.LoggerUtils;

public abstract class InAppBillingFragment extends AbstractGridFragment<Product> {
	
	private static final Logger LOGGER = LoggerUtils.getLogger(InAppBillingFragment.class);
	
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
		inAppBillingClient.startSetup(new InAppBillingClient.InAppBillingSetupListener() {
			
			@Override
			public void onSetupFinished() {
				if (inAppBillingClient != null) {
					
					// IAB is fully set up. Now, let's get an inventory of stuff we own.
					LOGGER.debug("Setup successful. Querying inventory.");
					
					List<String> productsIds = Lists.newArrayList();
					for (ProductType each : getProductTypes()) {
						productsIds.add(each.getProductId());
					}
					inAppBillingClient.queryInventoryAsync(true, productsIds, queryInventoryListener);
				}
			}
			
			@Override
			public void onSetupFailed(ErrorCodeException errorCodeException) {
				if (inAppBillingClient != null) {
					AbstractApplication.get().getExceptionHandler().handleThrowable(errorCodeException);
				}
			}
		});
	}
	
	protected abstract List<? extends ProductType> getProductTypes();
	
	protected abstract ProductType getProductType(String productId);
	
	// Listener that's called when we finish querying the items and subscriptions we own
	private QueryInventoryFinishedListener queryInventoryListener = new InAppBillingClient.QueryInventoryFinishedListener() {
		
		@Override
		public void onQueryInventoryFinished(Inventory inventory) {
			if (inAppBillingClient != null) {
				LOGGER.debug("Query inventory was successful.");
				
				List<Product> productsToConsume = Lists.newArrayList();
				
				products = Lists.newArrayList();
				for (ProductType each : getProductTypes()) {
					SkuDetails skuDetails = inventory.getSkuDetails(each.getProductId());
					if (skuDetails != null) {
						ProductType productType = BillingContext.get().isInAppBillingMockEnabled() ? BillingContext.get().getTestProductType()
								: each;
						Product product = new Product(productType, skuDetails.getFormattedPrice(),
								skuDetails.getPrice(), skuDetails.getCurrencyCode(), getString(each.getTitleId()),
								getString(each.getDescriptionId(), skuDetails.getFormattedPrice()));
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
			}
		}
		
		@Override
		public void onQueryInventoryFailed(ErrorCodeException errorCodeException) {
			if (inAppBillingClient != null) {
				dismissLoading();
				
				errorCodeException.setDescription(getString(R.string.failedToLoadPurchases));
				AbstractApplication.get().getExceptionHandler().handleThrowable(errorCodeException);
			}
		}
	};
	
	/**
	 * This method is executed (on the UI thread) when the products are loaded
	 * 
	 * @param products The loaded products
	 */
	protected abstract void onProductsLoaded(List<Product> products);
	
	protected abstract void onPurchased(Product product);
	
	protected void onConsumed(Product product) {
		// Do nothing
	}
	
	public void launchPurchaseFlow(Product product) {
		inAppBillingClient.launchInAppPurchaseFlow(getActivity(), product.getProductType().getProductId(),
			purchaseListener, product.generatePayload());
		AbstractApplication.get().getAnalyticsSender().trackInAppBillingPurchaseTry(product);
	}
	
	// Callback for when a purchase is finished
	private InAppBillingPurchaseFinishedListener purchaseListener = new InAppBillingClient.InAppBillingPurchaseFinishedListener() {
		
		@Override
		public void onIabPurchaseFinished(Purchase purchase) {
			if (inAppBillingClient != null) {
				LOGGER.info("Purchase finished: + " + purchase);
				
				Product product = findProduct(purchase);
				
				if (product.verifyDeveloperPayload(purchase)) {
					product.setPurchase(purchase);
					
					AbstractApplication.get().getAnalyticsSender().trackInAppBillingPurchase(product);
					onPurchased(product);
					
					if (product.getProductType().isConsumable()) {
						inAppBillingClient.consumeAsync(purchase, consumeListener);
					}
				} else {
					ExecutorUtils.execute(new Runnable() {
						
						@Override
						public void run() {
							throw new UnexpectedException("Authenticity verification failed");
						}
					});
				}
			}
		}
		
		@Override
		public void onIabPurchaseFailed(ErrorCodeException errorCodeException) {
			if (DefaultExceptionHandler.matchAnyErrorCode(errorCodeException, InAppBillingErrorCode.USER_CANCELED)) {
				LOGGER.warn(errorCodeException.getMessage());
			} else if (DefaultExceptionHandler.matchAnyErrorCode(errorCodeException,
				InAppBillingErrorCode.DEVELOPER_ERROR)) {
				AbstractApplication.get().getExceptionHandler().logHandledException(errorCodeException);
			} else {
				AbstractApplication.get().getExceptionHandler().handleThrowable(errorCodeException);
			}
		}
	};
	
	protected void onFailedToPurchase() {
		ExecutorUtils.execute(new Runnable() {
			
			@Override
			public void run() {
				throw com.jdroid.java.exception.CommonErrorCode.UNEXPECTED_ERROR.newErrorCodeException();
			}
		});
	}
	
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
		public void onConsumeFinished(Purchase purchase) {
			if (inAppBillingClient != null) {
				Product product = findProduct(purchase);
				product.setPurchase(null);
				
				onConsumed(product);
			}
		}
		
		@Override
		public void onConsumeFailed(ErrorCodeException errorCodeException) {
			if (inAppBillingClient != null) {
				AbstractApplication.get().getExceptionHandler().handleThrowable(errorCodeException);
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
	
	/**
	 * @see com.jdroid.android.fragment.AbstractFragment#getDefaultLoading()
	 */
	@Override
	public FragmentLoading getDefaultLoading() {
		return new NonBlockingLoading();
	}
}
