/*
 * Copyright (c) 2012 Google Inc. Licensed under the Apache License, Version 2.0 (the "License"); you may not use this
 * file except in compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language governing permissions and limitations under the
 * License.
 */

package com.jdroid.android.inappbilling;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONException;
import org.slf4j.Logger;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.content.ServiceConnection;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.text.TextUtils;
import com.android.vending.billing.IInAppBillingService;
import com.jdroid.android.concurrent.SafeRunnable;
import com.jdroid.java.concurrent.ExecutorUtils;
import com.jdroid.java.utils.CollectionUtils;
import com.jdroid.java.utils.LoggerUtils;

/**
 * Provides convenience methods for in-app billing. You can create one instance of this class for your application and
 * use it to process in-app billing operations. It provides synchronous (blocking) and asynchronous (non-blocking)
 * methods for many common in-app billing operations, as well as automatic signature verification.
 * 
 * After instantiating, you must perform setup in order to start using the object. To perform setup, call the
 * {@link #startSetup} method and provide a listener; that listener will be notified when setup is complete, after which
 * (and not before) you may call other methods.
 * 
 * After setup is complete, you will typically want to request an inventory of owned items and subscriptions. See
 * {@link #queryInventory}, {@link #queryInventoryAsync} and related methods.
 * 
 * When you are done with this object, don't forget to call {@link #dispose} to ensure proper cleanup. This object holds
 * a binding to the in-app billing service, which will leak unless you dispose of it correctly. If you created the
 * object on an Activity's onCreate method, then the recommended place to dispose of it is the Activity's onDestroy
 * method.
 * 
 * A note about threading: When using this object from a background thread, you may call the blocking versions of
 * methods; when using from a UI thread, call only the asynchronous versions and handle the results via callbacks. Also,
 * notice that you can only call one asynchronous operation at a time; attempting to start a second asynchronous
 * operation while the first one has not yet completed will result in an exception being thrown.
 * 
 * @author Bruno Oliveira (Google)
 * 
 */
public class InAppBillingClient {
	
	private final static Logger LOGGER = LoggerUtils.getLogger(InAppBillingClient.class);
	
	// Is setup done?
	boolean mSetupDone = false;
	
	// Has this object been disposed of? (If so, we should ignore callbacks, etc)
	boolean mDisposed = false;
	
	// Are subscriptions supported?
	boolean mSubscriptionsSupported = false;
	
	// Is an asynchronous operation in progress?
	// (only one at a time can be in progress)
	boolean mAsyncInProgress = false;
	
	// (for logging/debugging)
	// if mAsyncInProgress == true, what asynchronous operation is in progress?
	String mAsyncOperation = "";
	
	// Context we were passed during initialization
	Context mContext;
	
	// Connection to the service
	IInAppBillingService mService;
	ServiceConnection mServiceConn;
	
	// The request code used to launch purchase flow
	int mRequestCode;
	
	// The item type of the current purchase flow
	String mPurchasingItemType;
	
	// Public key for verifying signature, in base64 encoding
	String mSignatureBase64 = null;
	
	// Keys for the responses from InAppBillingService
	public static final String RESPONSE_CODE = "RESPONSE_CODE";
	public static final String RESPONSE_GET_SKU_DETAILS_LIST = "DETAILS_LIST";
	public static final String RESPONSE_BUY_INTENT = "BUY_INTENT";
	public static final String RESPONSE_INAPP_PURCHASE_DATA = "INAPP_PURCHASE_DATA";
	public static final String RESPONSE_INAPP_SIGNATURE = "INAPP_DATA_SIGNATURE";
	public static final String RESPONSE_INAPP_ITEM_LIST = "INAPP_PURCHASE_ITEM_LIST";
	public static final String RESPONSE_INAPP_PURCHASE_DATA_LIST = "INAPP_PURCHASE_DATA_LIST";
	public static final String RESPONSE_INAPP_SIGNATURE_LIST = "INAPP_DATA_SIGNATURE_LIST";
	public static final String INAPP_CONTINUATION_TOKEN = "INAPP_CONTINUATION_TOKEN";
	
	// Item types
	public static final String ITEM_TYPE_INAPP = "inapp";
	public static final String ITEM_TYPE_SUBS = "subs";
	
	// some fields on the getSkuDetails response bundle
	public static final String GET_SKU_DETAILS_ITEM_LIST = "ITEM_ID_LIST";
	public static final String GET_SKU_DETAILS_ITEM_TYPE_LIST = "ITEM_TYPE_LIST";
	
	/**
	 * Creates an instance. After creation, it will not yet be ready to use. You must perform setup by calling
	 * {@link #startSetup} and wait for setup to complete. This constructor does not block and is safe to call from a UI
	 * thread.
	 * 
	 * @param ctx Your application or Activity context. Needed to bind to the in-app billing service.
	 * @param base64PublicKey Your application's public key, encoded in base64. This is used for verification of
	 *            purchase signatures. You can find your app's base64-encoded public key in your application's page on
	 *            Google Play Developer Console. Note that this is NOT your "developer public key".
	 */
	public InAppBillingClient(Context ctx, String base64PublicKey) {
		mContext = ctx.getApplicationContext();
		mSignatureBase64 = base64PublicKey;
		LOGGER.debug("IAB helper created.");
	}
	
	/**
	 * Callback for setup process. This listener's {@link #onIabSetupFinished} method is called when the setup process
	 * is complete.
	 */
	public interface OnIabSetupFinishedListener {
		
		/**
		 * Called to notify that setup is complete.
		 * 
		 * @param inAppBillingResponseCode The result of the setup process.
		 */
		public void onIabSetupFinished(InAppBillingResponseCode inAppBillingResponseCode);
		
	}
	
	/**
	 * Starts the setup process. This will start up the setup process asynchronously. You will be notified through the
	 * listener when the setup process is complete. This method is safe to call from a UI thread.
	 * 
	 * @param listener The listener to notify when the setup process is complete.
	 */
	public void startSetup(final OnIabSetupFinishedListener listener) {
		// If already set up, can't do it again.
		checkNotDisposed();
		if (mSetupDone) {
			throw new IllegalStateException("IAB helper is already set up.");
		}
		
		// Connection to IAB service
		LOGGER.debug("Starting in-app billing setup.");
		mServiceConn = new ServiceConnection() {
			
			@Override
			public void onServiceDisconnected(ComponentName name) {
				LOGGER.debug("Billing service disconnected.");
				mService = null;
			}
			
			@Override
			public void onServiceConnected(ComponentName name, IBinder service) {
				if (mDisposed) {
					return;
				}
				LOGGER.debug("Billing service connected.");
				mService = IInAppBillingService.Stub.asInterface(service);
				String packageName = mContext.getPackageName();
				try {
					LOGGER.debug("Checking for in-app billing 3 support.");
					
					// check for in-app billing v3 support
					int response = mService.isBillingSupported(3, packageName, ITEM_TYPE_INAPP);
					InAppBillingResponseCode responseCode = InAppBillingResponseCode.valueOf(response);
					if (responseCode != InAppBillingResponseCode.OK) {
						if (listener != null) {
							listener.onIabSetupFinished(responseCode);
						}
						
						// if in-app purchases aren't supported, neither are subscriptions.
						mSubscriptionsSupported = false;
						return;
					}
					LOGGER.debug("In-app billing version 3 supported for " + packageName);
					
					// check for v3 subscriptions support
					response = mService.isBillingSupported(3, packageName, ITEM_TYPE_SUBS);
					responseCode = InAppBillingResponseCode.valueOf(response);
					if (responseCode == InAppBillingResponseCode.OK) {
						LOGGER.debug("Subscriptions AVAILABLE.");
						mSubscriptionsSupported = true;
					} else {
						LOGGER.debug("Subscriptions NOT AVAILABLE. Response: " + response);
					}
					
					mSetupDone = true;
				} catch (RemoteException e) {
					if (listener != null) {
						listener.onIabSetupFinished(InAppBillingResponseCode.IABHELPER_REMOTE_EXCEPTION);
					}
					e.printStackTrace();
					return;
				}
				
				if (listener != null) {
					listener.onIabSetupFinished(InAppBillingResponseCode.OK);
				}
			}
		};
		
		Intent serviceIntent = new Intent("com.android.vending.billing.InAppBillingService.BIND");
		serviceIntent.setPackage("com.android.vending");
		List<ResolveInfo> resolveInfos = mContext.getPackageManager().queryIntentServices(serviceIntent, 0);
		if (CollectionUtils.isNotEmpty(resolveInfos)) {
			// service available to handle that Intent
			mContext.bindService(serviceIntent, mServiceConn, Context.BIND_AUTO_CREATE);
		} else {
			// no service available to handle that Intent
			if (listener != null) {
				listener.onIabSetupFinished(InAppBillingResponseCode.BILLING_UNAVAILABLE);
			}
		}
	}
	
	/**
	 * Dispose of object, releasing resources. It's very important to call this method when you are done with this
	 * object. It will release any resources used by it such as service connections. Naturally, once the object is
	 * disposed of, it can't be used again.
	 */
	public void dispose() {
		LOGGER.debug("Disposing.");
		mSetupDone = false;
		if (mServiceConn != null) {
			LOGGER.debug("Unbinding from service.");
			if (mContext != null) {
				try {
					mContext.unbindService(mServiceConn);
				} catch (RuntimeException e) {
					LOGGER.warn("Error on unbinding", e);
				}
			}
		}
		mDisposed = true;
		mContext = null;
		mServiceConn = null;
		mService = null;
		mPurchaseListener = null;
	}
	
	private void checkNotDisposed() {
		if (mDisposed) {
			throw new IllegalStateException("IabHelper was disposed of, so it cannot be used.");
		}
	}
	
	/**
	 * @return whether subscriptions are supported.
	 */
	public boolean subscriptionsSupported() {
		checkNotDisposed();
		return mSubscriptionsSupported;
	}
	
	/**
	 * Callback that notifies when a purchase is finished.
	 */
	public interface OnIabPurchaseFinishedListener {
		
		/**
		 * Called to notify that an in-app purchase finished. If the purchase was successful, then the sku parameter
		 * specifies which item was purchased. If the purchase failed, the sku and extraData parameters may or may not
		 * be null, depending on how far the purchase process went.
		 * 
		 * @param result The result of the purchase.
		 * @param info The purchase information (null if purchase failed)
		 */
		public void onIabPurchaseFinished(InAppBillingResponseCode result, Purchase info);
	}
	
	// The listener registered on launchPurchaseFlow, which we have to call back when
	// the purchase finishes
	OnIabPurchaseFinishedListener mPurchaseListener;
	
	public void launchPurchaseFlow(Activity act, String sku, int requestCode, OnIabPurchaseFinishedListener listener) {
		launchPurchaseFlow(act, sku, requestCode, listener, "");
	}
	
	public void launchPurchaseFlow(Activity act, String sku, int requestCode, OnIabPurchaseFinishedListener listener,
			String extraData) {
		launchPurchaseFlow(act, sku, ITEM_TYPE_INAPP, requestCode, listener, extraData);
	}
	
	public void launchSubscriptionPurchaseFlow(Activity act, String sku, int requestCode,
			OnIabPurchaseFinishedListener listener) {
		launchSubscriptionPurchaseFlow(act, sku, requestCode, listener, "");
	}
	
	public void launchSubscriptionPurchaseFlow(Activity act, String sku, int requestCode,
			OnIabPurchaseFinishedListener listener, String extraData) {
		launchPurchaseFlow(act, sku, ITEM_TYPE_SUBS, requestCode, listener, extraData);
	}
	
	/**
	 * Initiate the UI flow for an in-app purchase. Call this method to initiate an in-app purchase, which will involve
	 * bringing up the Google Play screen. The calling activity will be paused while the user interacts with Google
	 * Play, and the result will be delivered via the activity's {@link android.app.Activity#onActivityResult} method,
	 * at which point you must call this object's {@link #handleActivityResult} method to continue the purchase flow.
	 * This method MUST be called from the UI thread of the Activity.
	 * 
	 * @param act The calling activity.
	 * @param sku The sku of the item to purchase.
	 * @param itemType indicates if it's a product or a subscription (ITEM_TYPE_INAPP or ITEM_TYPE_SUBS)
	 * @param requestCode A request code (to differentiate from other responses -- as in
	 *            {@link android.app.Activity#startActivityForResult}).
	 * @param listener The listener to notify when the purchase process finishes
	 * @param extraData Extra data (developer payload), which will be returned with the purchase data when the purchase
	 *            completes. This extra data will be permanently bound to that purchase and will always be returned when
	 *            the purchase is queried.
	 */
	public void launchPurchaseFlow(Activity act, String sku, String itemType, int requestCode,
			OnIabPurchaseFinishedListener listener, String extraData) {
		checkNotDisposed();
		checkSetupDone("launchPurchaseFlow");
		flagStartAsync("launchPurchaseFlow");
		
		if (itemType.equals(ITEM_TYPE_SUBS) && !mSubscriptionsSupported) {
			flagEndAsync();
			if (listener != null) {
				listener.onIabPurchaseFinished(InAppBillingResponseCode.IABHELPER_SUBSCRIPTIONS_NOT_AVAILABLE, null);
			}
			return;
		}
		
		try {
			LOGGER.debug("Constructing buy intent for " + sku + ", item type: " + itemType);
			Bundle buyIntentBundle = mService.getBuyIntent(3, mContext.getPackageName(), sku, itemType, extraData);
			InAppBillingResponseCode responseCode = getResponseCodeFromBundle(buyIntentBundle);
			if (responseCode != InAppBillingResponseCode.OK) {
				LOGGER.error("Unable to buy item, Error response: " + responseCode.name());
				flagEndAsync();
				if (listener != null) {
					listener.onIabPurchaseFinished(responseCode, null);
				}
				return;
			}
			
			PendingIntent pendingIntent = buyIntentBundle.getParcelable(RESPONSE_BUY_INTENT);
			LOGGER.debug("Launching buy intent for " + sku + ". Request code: " + requestCode);
			mRequestCode = requestCode;
			mPurchaseListener = listener;
			mPurchasingItemType = itemType;
			act.startIntentSenderForResult(pendingIntent.getIntentSender(), requestCode, new Intent(),
				Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0));
		} catch (SendIntentException e) {
			LOGGER.error("SendIntentException while launching purchase flow for sku " + sku);
			e.printStackTrace();
			flagEndAsync();
			
			if (listener != null) {
				listener.onIabPurchaseFinished(InAppBillingResponseCode.IABHELPER_SEND_INTENT_FAILED, null);
			}
		} catch (RemoteException e) {
			LOGGER.error("RemoteException while launching purchase flow for sku " + sku);
			e.printStackTrace();
			flagEndAsync();
			
			if (listener != null) {
				listener.onIabPurchaseFinished(InAppBillingResponseCode.IABHELPER_REMOTE_EXCEPTION, null);
			}
		}
	}
	
	/**
	 * Handles an activity result that's part of the purchase flow in in-app billing. If you are calling
	 * {@link #launchPurchaseFlow}, then you must call this method from your Activity's onActivityResult method. This
	 * method MUST be called from the UI thread of the Activity.
	 * 
	 * @param requestCode The requestCode as you received it.
	 * @param resultCode The resultCode as you received it.
	 * @param data The data (Intent) as you received it.
	 * @return Returns true if the result was related to a purchase flow and was handled; false if the result was not
	 *         related to a purchase, in which case you should handle it normally.
	 */
	public boolean handleActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode != mRequestCode) {
			return false;
		}
		
		checkNotDisposed();
		checkSetupDone("handleActivityResult");
		
		// end of async purchase operation that started on launchPurchaseFlow
		flagEndAsync();
		
		if (data == null) {
			LOGGER.error("Null data in IAB activity result.");
			if (mPurchaseListener != null) {
				mPurchaseListener.onIabPurchaseFinished(InAppBillingResponseCode.IABHELPER_BAD_RESPONSE, null);
			}
			return true;
		}
		
		InAppBillingResponseCode responseCode = getResponseCodeFromIntent(data);
		String purchaseData = data.getStringExtra(RESPONSE_INAPP_PURCHASE_DATA);
		String dataSignature = data.getStringExtra(RESPONSE_INAPP_SIGNATURE);
		
		if ((resultCode == Activity.RESULT_OK) && (responseCode == InAppBillingResponseCode.OK)) {
			LOGGER.debug("Successful resultcode from purchase activity.");
			LOGGER.debug("Purchase data: " + purchaseData);
			LOGGER.debug("Data signature: " + dataSignature);
			LOGGER.debug("Extras: " + data.getExtras());
			LOGGER.debug("Expected item type: " + mPurchasingItemType);
			
			if ((purchaseData == null) || (dataSignature == null)) {
				LOGGER.error("BUG: either purchaseData or dataSignature is null.");
				LOGGER.debug("Extras: " + data.getExtras().toString());
				if (mPurchaseListener != null) {
					mPurchaseListener.onIabPurchaseFinished(InAppBillingResponseCode.IABHELPER_UNKNOWN_ERROR, null);
				}
				return true;
			}
			
			Purchase purchase = null;
			try {
				purchase = new Purchase(mPurchasingItemType, purchaseData, dataSignature);
				String sku = purchase.getSku();
				
				// Verify signature
				if (!Security.verifyPurchase(mSignatureBase64, purchaseData, dataSignature)) {
					LOGGER.error("Purchase signature verification FAILED for sku " + sku);
					if (mPurchaseListener != null) {
						mPurchaseListener.onIabPurchaseFinished(InAppBillingResponseCode.IABHELPER_VERIFICATION_FAILED,
							purchase);
					}
					return true;
				}
				LOGGER.debug("Purchase signature successfully verified.");
			} catch (JSONException e) {
				LOGGER.error("Failed to parse purchase data.");
				e.printStackTrace();
				if (mPurchaseListener != null) {
					mPurchaseListener.onIabPurchaseFinished(InAppBillingResponseCode.IABHELPER_BAD_RESPONSE, null);
				}
				return true;
			}
			
			if (mPurchaseListener != null) {
				mPurchaseListener.onIabPurchaseFinished(InAppBillingResponseCode.OK, purchase);
			}
		} else if (resultCode == Activity.RESULT_OK) {
			// result code was OK, but in-app billing response was not OK.
			LOGGER.debug("Result code was OK but in-app billing response was not OK: " + responseCode.name());
			if (mPurchaseListener != null) {
				mPurchaseListener.onIabPurchaseFinished(responseCode, null);
			}
		} else if (resultCode == Activity.RESULT_CANCELED) {
			LOGGER.debug("Purchase canceled - Response: " + responseCode.name());
			if (mPurchaseListener != null) {
				mPurchaseListener.onIabPurchaseFinished(InAppBillingResponseCode.IABHELPER_USER_CANCELLED, null);
			}
		} else {
			LOGGER.error("Purchase failed. Result code: " + Integer.toString(resultCode) + ". Response: "
					+ responseCode.name());
			if (mPurchaseListener != null) {
				mPurchaseListener.onIabPurchaseFinished(InAppBillingResponseCode.IABHELPER_UNKNOWN_PURCHASE_RESPONSE,
					null);
			}
		}
		return true;
	}
	
	public Inventory queryInventory(boolean querySkuDetails, List<String> moreSkus) throws IabException {
		return queryInventory(querySkuDetails, moreSkus, null);
	}
	
	/**
	 * Queries the inventory. This will query all owned items from the server, as well as information on additional
	 * skus, if specified. This method may block or take long to execute. Do not call from a UI thread. For that, use
	 * the non-blocking version refreshInventoryAsync.
	 * 
	 * @param querySkuDetails if true, SKU details (price, description, etc) will be queried as well as purchase
	 *            information.
	 * @param moreItemSkus additional PRODUCT skus to query information on, regardless of ownership. Ignored if null or
	 *            if querySkuDetails is false.
	 * @param moreSubsSkus additional SUBSCRIPTIONS skus to query information on, regardless of ownership. Ignored if
	 *            null or if querySkuDetails is false.
	 * @return The {@link Inventory}
	 * @throws IabException if a problem occurs while refreshing the inventory.
	 */
	public Inventory queryInventory(boolean querySkuDetails, List<String> moreItemSkus, List<String> moreSubsSkus)
			throws IabException {
		try {
			Inventory inv = null;
			if (!mDisposed && mSetupDone) {
				inv = new Inventory();
				InAppBillingResponseCode responseCode = queryPurchases(inv, ITEM_TYPE_INAPP);
				if (responseCode != InAppBillingResponseCode.OK) {
					LOGGER.error("Error refreshing inventory (querying owned items).");
					throw new IabException(responseCode);
				}
				
				if (querySkuDetails) {
					responseCode = querySkuDetails(ITEM_TYPE_INAPP, inv, moreItemSkus);
					if (responseCode != InAppBillingResponseCode.OK) {
						LOGGER.error("Error refreshing inventory (querying prices of items).");
						throw new IabException(responseCode);
					}
				}
				
				// if subscriptions are supported, then also query for subscriptions
				if (mSubscriptionsSupported) {
					responseCode = queryPurchases(inv, ITEM_TYPE_SUBS);
					if (responseCode != InAppBillingResponseCode.OK) {
						LOGGER.error("Error refreshing inventory (querying owned subscriptions).");
						throw new IabException(responseCode);
					}
					
					if (querySkuDetails) {
						responseCode = querySkuDetails(ITEM_TYPE_SUBS, inv, moreItemSkus);
						if (responseCode != InAppBillingResponseCode.OK) {
							LOGGER.error("Error refreshing inventory (querying prices of subscriptions).");
							throw new IabException(responseCode);
						}
					}
				}
			} else {
				LOGGER.warn("Client disposed. Not queried inventary");
			}
			
			return inv;
		} catch (RemoteException e) {
			LOGGER.error("Remote exception while refreshing inventory.", e);
			throw new IabException(InAppBillingResponseCode.IABHELPER_REMOTE_EXCEPTION);
		} catch (JSONException e) {
			LOGGER.error("Error parsing JSON response while refreshing inventory.", e);
			throw new IabException(InAppBillingResponseCode.IABHELPER_BAD_RESPONSE);
		}
	}
	
	/**
	 * Listener that notifies when an inventory query operation completes.
	 */
	public interface QueryInventoryFinishedListener {
		
		/**
		 * Called to notify that an inventory query operation completed.
		 * 
		 * @param result The result of the operation.
		 * @param inv The inventory.
		 */
		public void onQueryInventoryFinished(InAppBillingResponseCode result, Inventory inv);
	}
	
	/**
	 * Asynchronous wrapper for inventory query. This will perform an inventory query as described in
	 * {@link #queryInventory}, but will do so asynchronously and call back the specified listener upon completion. This
	 * method is safe to call from a UI thread.
	 * 
	 * @param querySkuDetails as in {@link #queryInventory}
	 * @param moreSkus as in {@link #queryInventory}
	 * @param listener The listener to notify when the refresh operation completes.
	 */
	public void queryInventoryAsync(final boolean querySkuDetails, final List<String> moreSkus,
			final QueryInventoryFinishedListener listener) {
		final Handler handler = new Handler();
		flagStartAsync("refresh inventory");
		ExecutorUtils.execute(new SafeRunnable() {
			
			@Override
			public void doRun() {
				InAppBillingResponseCode result = InAppBillingResponseCode.OK;
				Inventory inv = null;
				try {
					inv = queryInventory(querySkuDetails, moreSkus);
				} catch (IabException ex) {
					result = ex.getInAppBillingResponseCode();
				}
				
				flagEndAsync();
				
				final InAppBillingResponseCode result_f = result;
				final Inventory inv_f = inv;
				if (!mDisposed && (listener != null)) {
					handler.post(new Runnable() {
						
						@Override
						public void run() {
							listener.onQueryInventoryFinished(result_f, inv_f);
						}
					});
				}
			}
		});
	}
	
	/**
	 * Consumes a given in-app product. Consuming can only be done on an item that's owned, and as a result of
	 * consumption, the user will no longer own it. This method may block or take long to return. Do not call from the
	 * UI thread. For that, see {@link #consumeAsync}.
	 * 
	 * @param itemInfo The PurchaseInfo that represents the item to consume.
	 */
	private InAppBillingResponseCode consume(Purchase itemInfo) {
		
		InAppBillingResponseCode responseCode = null;
		
		if (itemInfo.getItemType().equals(ITEM_TYPE_INAPP)) {
			responseCode = InAppBillingResponseCode.OK;
		} else {
			LOGGER.error("Items of type '" + itemInfo.getItemType() + "' can't be consumed.");
			responseCode = InAppBillingResponseCode.IABHELPER_INVALID_CONSUMPTION;
		}
		
		try {
			String token = itemInfo.getToken();
			String sku = itemInfo.getSku();
			if ((token == null) || token.equals("")) {
				LOGGER.error("Can't consume " + sku + ". No token.");
				responseCode = InAppBillingResponseCode.IABHELPER_MISSING_TOKEN;
			}
			
			if (!mDisposed) {
				LOGGER.debug("Consuming sku: " + sku + ", token: " + token);
				int response = mService.consumePurchase(3, mContext.getPackageName(), token);
				responseCode = InAppBillingResponseCode.valueOf(response);
				if (responseCode == InAppBillingResponseCode.OK) {
					LOGGER.debug("Successfully consumed sku: " + sku);
				} else {
					LOGGER.error("Error consuming consuming sku " + sku + ". " + responseCode.name());
				}
			} else {
				LOGGER.warn("Client disposed. Not consuming sku: " + sku + ", token: " + token);
			}
		} catch (RemoteException e) {
			LOGGER.error("Remote exception while consuming. PurchaseInfo: " + itemInfo, e);
			responseCode = InAppBillingResponseCode.IABHELPER_REMOTE_EXCEPTION;
		}
		return responseCode;
	}
	
	/**
	 * Callback that notifies when a consumption operation finishes.
	 */
	public interface OnConsumeFinishedListener {
		
		/**
		 * Called to notify that a consumption has finished.
		 * 
		 * @param purchase The purchase that was (or was to be) consumed.
		 * @param result The result of the consumption operation.
		 */
		public void onConsumeFinished(Purchase purchase, InAppBillingResponseCode result);
	}
	
	/**
	 * Callback that notifies when a multi-item consumption operation finishes.
	 */
	public interface OnConsumeMultiFinishedListener {
		
		/**
		 * Called to notify that a consumption of multiple items has finished.
		 * 
		 * @param purchases The purchases that were (or were to be) consumed.
		 * @param results The results of each consumption operation, corresponding to each sku.
		 */
		public void onConsumeMultiFinished(List<Purchase> purchases, List<InAppBillingResponseCode> results);
	}
	
	/**
	 * Asynchronous wrapper to item consumption. Works like {@link #consume}, but performs the consumption in the
	 * background and notifies completion through the provided listener. This method is safe to call from a UI thread.
	 * 
	 * @param purchase The purchase to be consumed.
	 * @param listener The listener to notify when the consumption operation finishes.
	 */
	public void consumeAsync(Purchase purchase, OnConsumeFinishedListener listener) {
		List<Purchase> purchases = new ArrayList<Purchase>();
		purchases.add(purchase);
		consumeAsyncInternal(purchases, listener, null);
	}
	
	/**
	 * Same as consumeAsync, but for multiple items at once.
	 * 
	 * @param purchases The list of PurchaseInfo objects representing the purchases to consume.
	 * @param listener The listener to notify when the consumption operation finishes.
	 */
	public void consumeAsync(List<Purchase> purchases, OnConsumeMultiFinishedListener listener) {
		consumeAsyncInternal(purchases, null, listener);
	}
	
	// Checks that setup was done; if not, throws an exception.
	private void checkSetupDone(String operation) {
		if (!mSetupDone) {
			LOGGER.error("Illegal state for operation (" + operation + "): IAB helper is not set up.");
			throw new IllegalStateException("IAB helper is not set up. Can't perform operation: " + operation);
		}
	}
	
	// Workaround to bug where sometimes response codes come as Long instead of Integer
	private InAppBillingResponseCode getResponseCodeFromBundle(Bundle b) {
		Object o = b.get(RESPONSE_CODE);
		if (o == null) {
			LOGGER.debug("Bundle/Intent with null response code, assuming OK (known issue)");
			return InAppBillingResponseCode.OK;
		} else if (o instanceof Integer) {
			return InAppBillingResponseCode.valueOf(((Integer)o).intValue());
		} else if (o instanceof Long) {
			return InAppBillingResponseCode.valueOf((int)((Long)o).longValue());
		} else {
			LOGGER.error("Unexpected type for bundle/intent response code.");
			LOGGER.error(o.getClass().getName());
			throw new RuntimeException("Unexpected type for bundle/intent response code: " + o.getClass().getName());
		}
	}
	
	// Workaround to bug where sometimes response codes come as Long instead of Integer
	private InAppBillingResponseCode getResponseCodeFromIntent(Intent i) {
		return getResponseCodeFromBundle(i.getExtras());
	}
	
	private void flagStartAsync(String operation) {
		if (mAsyncInProgress) {
			throw new IllegalStateException("Can't start async operation (" + operation
					+ ") because another async operation(" + mAsyncOperation + ") is in progress.");
		}
		mAsyncOperation = operation;
		mAsyncInProgress = true;
		LOGGER.debug("Starting async operation: " + operation);
	}
	
	private void flagEndAsync() {
		LOGGER.debug("Ending async operation: " + mAsyncOperation);
		mAsyncOperation = "";
		mAsyncInProgress = false;
	}
	
	private InAppBillingResponseCode queryPurchases(Inventory inv, String itemType) throws JSONException,
			RemoteException {
		// Query purchases
		LOGGER.debug("Querying owned items, item type: " + itemType);
		LOGGER.debug("Package name: " + mContext.getPackageName());
		boolean verificationFailed = false;
		String continueToken = null;
		
		do {
			LOGGER.debug("Calling getPurchases with continuation token: " + continueToken);
			Bundle ownedItems = mService.getPurchases(3, mContext.getPackageName(), itemType, continueToken);
			
			InAppBillingResponseCode responseCode = getResponseCodeFromBundle(ownedItems);
			LOGGER.debug("Owned items response: " + responseCode.name());
			if (responseCode != InAppBillingResponseCode.OK) {
				LOGGER.debug("getPurchases() failed: " + responseCode.name());
				return responseCode;
			}
			if (!ownedItems.containsKey(RESPONSE_INAPP_ITEM_LIST)
					|| !ownedItems.containsKey(RESPONSE_INAPP_PURCHASE_DATA_LIST)
					|| !ownedItems.containsKey(RESPONSE_INAPP_SIGNATURE_LIST)) {
				LOGGER.error("Bundle returned from getPurchases() doesn't contain required fields.");
				return InAppBillingResponseCode.IABHELPER_BAD_RESPONSE;
			}
			
			ArrayList<String> ownedSkus = ownedItems.getStringArrayList(RESPONSE_INAPP_ITEM_LIST);
			ArrayList<String> purchaseDataList = ownedItems.getStringArrayList(RESPONSE_INAPP_PURCHASE_DATA_LIST);
			ArrayList<String> signatureList = ownedItems.getStringArrayList(RESPONSE_INAPP_SIGNATURE_LIST);
			
			for (int i = 0; i < purchaseDataList.size(); ++i) {
				String purchaseData = purchaseDataList.get(i);
				String signature = signatureList.get(i);
				String sku = ownedSkus.get(i);
				if (Security.verifyPurchase(mSignatureBase64, purchaseData, signature)) {
					LOGGER.debug("Sku is owned: " + sku);
					Purchase purchase = new Purchase(itemType, purchaseData, signature);
					
					if (TextUtils.isEmpty(purchase.getToken())) {
						LOGGER.warn("BUG: empty/null token!");
						LOGGER.debug("Purchase data: " + purchaseData);
					}
					
					// Record ownership and token
					inv.addPurchase(purchase);
				} else {
					LOGGER.warn("Purchase signature verification **FAILED**. Not adding item.");
					LOGGER.debug("   Purchase data: " + purchaseData);
					LOGGER.debug("   Signature: " + signature);
					verificationFailed = true;
				}
			}
			
			continueToken = ownedItems.getString(INAPP_CONTINUATION_TOKEN);
			LOGGER.debug("Continuation token: " + continueToken);
		} while (!TextUtils.isEmpty(continueToken));
		
		return verificationFailed ? InAppBillingResponseCode.IABHELPER_VERIFICATION_FAILED
				: InAppBillingResponseCode.OK;
	}
	
	private InAppBillingResponseCode querySkuDetails(String itemType, Inventory inv, List<String> moreSkus)
			throws RemoteException, JSONException {
		LOGGER.debug("Querying SKU details.");
		ArrayList<String> skuList = new ArrayList<String>();
		skuList.addAll(inv.getAllOwnedSkus(itemType));
		if (moreSkus != null) {
			for (String sku : moreSkus) {
				if (!skuList.contains(sku)) {
					skuList.add(sku);
				}
			}
		}
		
		if (skuList.size() == 0) {
			LOGGER.debug("queryPrices: nothing to do because there are no SKUs.");
			return InAppBillingResponseCode.OK;
		}
		
		Bundle querySkus = new Bundle();
		querySkus.putStringArrayList(GET_SKU_DETAILS_ITEM_LIST, skuList);
		Bundle skuDetails = mService.getSkuDetails(3, mContext.getPackageName(), itemType, querySkus);
		
		if (!skuDetails.containsKey(RESPONSE_GET_SKU_DETAILS_LIST)) {
			InAppBillingResponseCode responseCode = getResponseCodeFromBundle(skuDetails);
			if (responseCode != InAppBillingResponseCode.OK) {
				LOGGER.debug("getSkuDetails() failed: " + responseCode.name());
				return responseCode;
			} else {
				LOGGER.error("getSkuDetails() returned a bundle with neither an error nor a detail list.");
				return InAppBillingResponseCode.IABHELPER_BAD_RESPONSE;
			}
		}
		
		ArrayList<String> responseList = skuDetails.getStringArrayList(RESPONSE_GET_SKU_DETAILS_LIST);
		
		for (String thisResponse : responseList) {
			SkuDetails d = new SkuDetails(itemType, thisResponse);
			LOGGER.debug("Got sku details: " + d);
			inv.addSkuDetails(d);
		}
		return InAppBillingResponseCode.OK;
	}
	
	private void consumeAsyncInternal(final List<Purchase> purchases, final OnConsumeFinishedListener singleListener,
			final OnConsumeMultiFinishedListener multiListener) {
		final Handler handler = new Handler();
		flagStartAsync("consume");
		ExecutorUtils.execute(new SafeRunnable() {
			
			@Override
			public void doRun() {
				final List<InAppBillingResponseCode> results = new ArrayList<InAppBillingResponseCode>();
				for (Purchase purchase : purchases) {
					InAppBillingResponseCode response = consume(purchase);
					if (response != null) {
						results.add(response);
					}
				}
				
				flagEndAsync();
				if (!mDisposed && (singleListener != null)) {
					handler.post(new Runnable() {
						
						@Override
						public void run() {
							singleListener.onConsumeFinished(purchases.get(0), results.get(0));
						}
					});
				}
				if (!mDisposed && (multiListener != null)) {
					handler.post(new Runnable() {
						
						@Override
						public void run() {
							multiListener.onConsumeMultiFinished(purchases, results);
						}
					});
				}
			}
		});
	}
}
