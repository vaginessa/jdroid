package com.jdroid.android.inappbilling;

import java.io.Serializable;
import java.util.List;
import org.slf4j.Logger;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import com.jdroid.android.AbstractApplication;
import com.jdroid.android.R;
import com.jdroid.android.activity.AbstractFragmentActivity;
import com.jdroid.android.exception.DefaultExceptionHandler;
import com.jdroid.android.fragment.AbstractFragment;
import com.jdroid.java.exception.ErrorCodeException;
import com.jdroid.java.utils.LoggerUtils;

public class InAppBillingHelperFragment extends AbstractFragment implements InAppBillingClientListener {
	
	private static final String MANAGED_PRODUCT_TYPES = "managedProductTypes";
	private static final String SUBSCRIPTIONS_PRODUCT_TYPES = "subscriptionsProductTypes";
	private static final String SILENT_MODE = "silentMode";
	
	private static final Logger LOGGER = LoggerUtils.getLogger(InAppBillingHelperFragment.class);
	
	private InAppBillingClient inAppBillingClient;
	private List<ProductType> managedProductTypes;
	private List<ProductType> subscriptionsProductTypes;
	private Boolean silentMode;
	
	public static void add(FragmentActivity activity,
			Class<? extends InAppBillingHelperFragment> inAppBillingHelperFragmentClass, Boolean silentMode,
			Fragment targetFragment) {
		add(activity, inAppBillingHelperFragmentClass, AbstractApplication.get().getManagedProductTypes(),
			AbstractApplication.get().getSubscriptionsProductTypes(), silentMode, targetFragment);
	}
	
	public static void add(FragmentActivity activity,
			Class<? extends InAppBillingHelperFragment> inAppBillingHelperFragmentClass,
			List<ProductType> managedProductTypes, List<ProductType> subscriptionsProductTypes, Boolean silentMode,
			Fragment targetFragment) {
		
		if (!managedProductTypes.isEmpty() || (!subscriptionsProductTypes.isEmpty() && (get(activity) == null))) {
			AbstractFragmentActivity abstractFragmentActivity = (AbstractFragmentActivity)activity;
			InAppBillingHelperFragment inAppBillingHelperFragment = abstractFragmentActivity.instanceFragment(
				inAppBillingHelperFragmentClass, null);
			inAppBillingHelperFragment.setTargetFragment(targetFragment, 0);
			
			Bundle args = new Bundle();
			args.putSerializable(MANAGED_PRODUCT_TYPES, (Serializable)managedProductTypes);
			args.putSerializable(SUBSCRIPTIONS_PRODUCT_TYPES, (Serializable)subscriptionsProductTypes);
			args.putBoolean(SILENT_MODE, silentMode);
			inAppBillingHelperFragment.setArguments(args);
			
			FragmentTransaction fragmentTransaction = abstractFragmentActivity.getSupportFragmentManager().beginTransaction();
			fragmentTransaction.add(0, inAppBillingHelperFragment, InAppBillingHelperFragment.class.getSimpleName());
			fragmentTransaction.commit();
		}
		
	}
	
	/**
	 * @see com.jdroid.android.fragment.AbstractFragment#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		managedProductTypes = getArgument(MANAGED_PRODUCT_TYPES);
		subscriptionsProductTypes = getArgument(SUBSCRIPTIONS_PRODUCT_TYPES);
		silentMode = getArgument(SILENT_MODE);
		
		inAppBillingClient = new InAppBillingClient(getActivity());
		
		// TODO The use cases logic should be replicated here. With the current approach, if an error happens on while
		// the app is on the background, when the fragment is resumed, the error dialog is not displayed to the user
		inAppBillingClient.setInAppBillingClientListener(this);
		inAppBillingClient.startSetup();
	}
	
	/**
	 * @see com.jdroid.android.inappbilling.InAppBillingClientListener#onSetupFinished()
	 */
	@Override
	public void onSetupFinished() {
		inAppBillingClient.queryInventory(managedProductTypes, subscriptionsProductTypes);
	}
	
	public InAppBillingListener getInAppBillingListener() {
		return (InAppBillingListener)getTargetFragment();
	}
	
	/**
	 * @see com.jdroid.android.inappbilling.InAppBillingClientListener#onSetupFailed(com.jdroid.java.exception.ErrorCodeException)
	 */
	@Override
	public void onSetupFailed(ErrorCodeException errorCodeException) {
		if (silentMode) {
			AbstractApplication.get().getExceptionHandler().logHandledException(errorCodeException);
		} else {
			AbstractApplication.get().getExceptionHandler().handleThrowable(errorCodeException);
		}
	}
	
	/**
	 * @see com.jdroid.android.inappbilling.InAppBillingClientListener#onQueryInventoryFinished(com.jdroid.android.inappbilling.Inventory)
	 */
	@Override
	public void onQueryInventoryFinished(Inventory inventory) {
		InAppBillingListener inAppBillingListener = getInAppBillingListener();
		if (inAppBillingListener != null) {
			inAppBillingListener.onProductsLoaded(inventory.getProducts());
			for (Product each : inventory.getProductToConsume()) {
				inAppBillingClient.consume(each);
			}
		}
	}
	
	/**
	 * @see com.jdroid.android.inappbilling.InAppBillingClientListener#onQueryInventoryFailed(com.jdroid.java.exception.ErrorCodeException)
	 */
	@Override
	public void onQueryInventoryFailed(ErrorCodeException errorCodeException) {
		if (silentMode) {
			AbstractApplication.get().getExceptionHandler().logHandledException(errorCodeException);
		} else {
			errorCodeException.setDescription(getString(R.string.failedToLoadPurchases));
			AbstractApplication.get().getExceptionHandler().handleThrowable(errorCodeException);
		}
	}
	
	public void launchPurchaseFlow(Product product) {
		inAppBillingClient.launchInAppPurchaseFlow(getActivity(), product.getId(), product.getDeveloperPayload());
		AbstractApplication.get().getAnalyticsSender().trackInAppBillingPurchaseTry(product);
	}
	
	/**
	 * @see com.jdroid.android.inappbilling.InAppBillingClientListener#onPurchaseFinished(com.jdroid.android.inappbilling.Product)
	 */
	@Override
	public void onPurchaseFinished(final Product product) {
		InAppBillingListener inAppBillingListener = getInAppBillingListener();
		if (inAppBillingListener != null) {
			inAppBillingListener.onPurchased(product);
			if (product.isWaitingToConsume()) {
				inAppBillingClient.consume(product);
			}
		}
	}
	
	/**
	 * @see com.jdroid.android.inappbilling.InAppBillingClientListener#onPurchaseFailed(com.jdroid.java.exception.ErrorCodeException)
	 */
	@Override
	public void onPurchaseFailed(ErrorCodeException errorCodeException) {
		if (DefaultExceptionHandler.matchAnyErrorCode(errorCodeException, InAppBillingErrorCode.USER_CANCELED)) {
			LOGGER.warn(errorCodeException.getMessage());
		} else if (DefaultExceptionHandler.matchAnyErrorCode(errorCodeException, InAppBillingErrorCode.DEVELOPER_ERROR,
			InAppBillingErrorCode.ITEM_UNAVAILABLE)) {
			AbstractApplication.get().getExceptionHandler().logHandledException(errorCodeException);
		} else {
			DefaultExceptionHandler.markAsNotGoBackOnError(errorCodeException);
			AbstractApplication.get().getExceptionHandler().handleThrowable(errorCodeException);
		}
	}
	
	/**
	 * @see com.jdroid.android.inappbilling.InAppBillingClientListener#onConsumeFinished(com.jdroid.android.inappbilling.Product)
	 */
	@Override
	public void onConsumeFinished(Product product) {
		InAppBillingListener inAppBillingListener = getInAppBillingListener();
		if (inAppBillingListener != null) {
			inAppBillingListener.onConsumed(product);
		}
	}
	
	@Override
	public void onConsumeFailed(ErrorCodeException errorCodeException) {
		if (silentMode) {
			AbstractApplication.get().getExceptionHandler().handleThrowable(errorCodeException);
		} else {
			AbstractApplication.get().getExceptionHandler().logHandledException(errorCodeException);
		}
	}
	
	/**
	 * @see android.support.v4.app.Fragment#onActivityResult(int, int, android.content.Intent)
	 */
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (inAppBillingClient != null) {
			// Pass on the activity result to the helper for handling
			if (!inAppBillingClient.handleActivityResult(requestCode, resultCode, data)) {
				// not handled, so handle it ourselves (here's where you'd perform any handling of activity results not
				// related to in-app billing...
				super.onActivityResult(requestCode, resultCode, data);
			}
		} else {
			super.onActivityResult(requestCode, resultCode, data);
		}
	}
	
	/**
	 * @see com.jdroid.android.fragment.AbstractFragment#onDestroy()
	 */
	@Override
	public void onDestroy() {
		super.onDestroy();
		
		if (inAppBillingClient != null) {
			inAppBillingClient.dispose();
			inAppBillingClient = null;
		}
	}
	
	public static InAppBillingHelperFragment get(FragmentActivity activity) {
		return ((AbstractFragmentActivity)activity).getFragment(InAppBillingHelperFragment.class);
	}
	
	public static void removeTarget(FragmentActivity activity) {
		InAppBillingHelperFragment inAppBillingHelperFragment = InAppBillingHelperFragment.get(activity);
		if (inAppBillingHelperFragment != null) {
			inAppBillingHelperFragment.setTargetFragment(null, 0);
		}
	}
}
