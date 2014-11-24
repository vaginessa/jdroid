package com.jdroid.android.inappbilling;

import java.util.List;
import org.slf4j.Logger;
import android.content.Intent;
import android.os.Bundle;
import com.jdroid.android.AbstractApplication;
import com.jdroid.android.R;
import com.jdroid.android.exception.DefaultExceptionHandler;
import com.jdroid.android.fragment.AbstractGridFragment;
import com.jdroid.android.inappbilling.InAppBillingClient.InAppBillingPurchaseFinishedListener;
import com.jdroid.android.inappbilling.InAppBillingClient.OnConsumeFinishedListener;
import com.jdroid.android.inappbilling.InAppBillingClient.QueryInventoryFinishedListener;
import com.jdroid.android.loading.FragmentLoading;
import com.jdroid.android.loading.NonBlockingLoading;
import com.jdroid.java.collections.Lists;
import com.jdroid.java.exception.ErrorCodeException;
import com.jdroid.java.utils.LoggerUtils;

public abstract class InAppBillingFragment extends AbstractGridFragment<Product> {
	
	private static final Logger LOGGER = LoggerUtils.getLogger(InAppBillingFragment.class);
	
	private InAppBillingClient inAppBillingClient;
	
	/**
	 * @see com.jdroid.android.activity.AbstractActivity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		inAppBillingClient = new InAppBillingClient(getActivity());
		
		// Start setup. This is asynchronous and the specified listener will be called once setup completes.
		inAppBillingClient.startSetup(new InAppBillingClient.InAppBillingSetupListener() {
			
			@Override
			public void onSetupFinished() {
				if (inAppBillingClient != null) {
					
					// IAB is fully set up. Now, let's get an inventory of stuff we own.
					LOGGER.debug("Setup successful. Querying inventory.");
					inAppBillingClient.queryInventory(getManagedProductTypes(), getSubscriptionsProductTypes(),
						queryInventoryListener);
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
	
	protected abstract List<ProductType> getManagedProductTypes();
	
	protected List<ProductType> getSubscriptionsProductTypes() {
		return Lists.newArrayList();
	}
	
	protected abstract ProductType getProductType(String productId);
	
	// Listener that's called when we finish querying the items and subscriptions we own
	private QueryInventoryFinishedListener queryInventoryListener = new InAppBillingClient.QueryInventoryFinishedListener() {
		
		@Override
		public void onQueryInventoryFinished(Inventory inventory) {
			if (inAppBillingClient != null) {
				LOGGER.debug("Query inventory was successful.");
				
				onProductsLoaded(inventory.getProducts());
				
				for (Product each : inventory.getProductToConsume()) {
					inAppBillingClient.consume(each, consumeListener);
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
		inAppBillingClient.launchInAppPurchaseFlow(getActivity(), product.getId(), purchaseListener,
			product.getDevloperPayload());
		AbstractApplication.get().getAnalyticsSender().trackInAppBillingPurchaseTry(product);
	}
	
	// Callback for when a purchase is finished
	private InAppBillingPurchaseFinishedListener purchaseListener = new InAppBillingClient.InAppBillingPurchaseFinishedListener() {
		
		@Override
		public void onPurchaseFinished(final Product product) {
			if (inAppBillingClient != null) {
				AbstractApplication.get().getAnalyticsSender().trackInAppBillingPurchase(product);
				onPurchased(product);
				
				if (product.isWaitingToConsume()) {
					inAppBillingClient.consume(product, consumeListener);
				}
			}
		}
		
		@Override
		public void onPurchaseFailed(ErrorCodeException errorCodeException) {
			if (DefaultExceptionHandler.matchAnyErrorCode(errorCodeException, InAppBillingErrorCode.USER_CANCELED)) {
				LOGGER.warn(errorCodeException.getMessage());
			} else if (DefaultExceptionHandler.matchAnyErrorCode(errorCodeException,
				InAppBillingErrorCode.DEVELOPER_ERROR, InAppBillingErrorCode.ITEM_UNAVAILABLE)) {
				AbstractApplication.get().getExceptionHandler().logHandledException(errorCodeException);
			} else {
				DefaultExceptionHandler.markAsNotGoBackOnError(errorCodeException);
				AbstractApplication.get().getExceptionHandler().handleThrowable(errorCodeException);
			}
		}
	};
	
	// Called when consumption is complete
	private OnConsumeFinishedListener consumeListener = new InAppBillingClient.OnConsumeFinishedListener() {
		
		@Override
		public void onConsumeFinished(Product product) {
			if (inAppBillingClient != null) {
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
