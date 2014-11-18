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
import com.jdroid.android.AbstractApplication;
import com.jdroid.android.concurrent.SafeRunnable;
import com.jdroid.java.collections.Lists;
import com.jdroid.java.concurrent.ExecutorUtils;
import com.jdroid.java.exception.ErrorCode;
import com.jdroid.java.exception.ErrorCodeException;
import com.jdroid.java.exception.UnexpectedException;
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
 */
public class InAppBillingClient {
	
	private final static Logger LOGGER = LoggerUtils.getLogger(InAppBillingClient.class);
	
	private static final int IN_APP_BILLING_API_VERSION = 3;
	
	// Keys for the responses from InAppBillingService
	private static final String RESPONSE_CODE = "RESPONSE_CODE";
	private static final String RESPONSE_GET_SKU_DETAILS_LIST = "DETAILS_LIST";
	private static final String RESPONSE_BUY_INTENT = "BUY_INTENT";
	private static final String RESPONSE_INAPP_PURCHASE_DATA = "INAPP_PURCHASE_DATA";
	private static final String RESPONSE_INAPP_SIGNATURE = "INAPP_DATA_SIGNATURE";
	private static final String RESPONSE_INAPP_ITEM_LIST = "INAPP_PURCHASE_ITEM_LIST";
	private static final String RESPONSE_INAPP_PURCHASE_DATA_LIST = "INAPP_PURCHASE_DATA_LIST";
	private static final String RESPONSE_INAPP_SIGNATURE_LIST = "INAPP_DATA_SIGNATURE_LIST";
	private static final String INAPP_CONTINUATION_TOKEN = "INAPP_CONTINUATION_TOKEN";
	
	public static final int PURCHASE_REQUEST_CODE = 10001;
	
	// Item types
	private static final String ITEM_TYPE_INAPP = "inapp";
	private static final String ITEM_TYPE_SUBS = "subs";
	
	// some fields on the getSkuDetails response bundle
	private static final String GET_SKU_DETAILS_ITEM_LIST = "ITEM_ID_LIST";
	
	// Is setup done?
	private boolean setupDone = false;
	
	// Has this object been disposed of? (If so, we should ignore callbacks, etc)
	private boolean disposed = false;
	
	// Are subscriptions supported?
	private boolean subscriptionsSupported = false;
	
	// Is an asynchronous operation in progress?
	// (only one at a time can be in progress)
	private boolean asyncInProgress = false;
	
	private String asyncOperation = "";
	
	// Context we were passed during initialization
	private Context context;
	
	// Connection to the service
	private IInAppBillingService service;
	private ServiceConnection serviceConnection;
	
	// The request code used to launch purchase flow
	private int requestCode;
	
	// The item type of the current purchase flow
	private String purchasingItemType;
	
	// Public key for verifying signature, in base64 encoding
	private String signatureBase64 = null;
	
	// The listener registered on launchPurchaseFlow, which we have to call back when
	// the purchase finishes
	private InAppBillingPurchaseFinishedListener purchaseListener;
	
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
		context = ctx.getApplicationContext();
		signatureBase64 = base64PublicKey;
		LOGGER.debug("InAppBillingClient created.");
	}
	
	/**
	 * Callback for setup process.
	 */
	public interface InAppBillingSetupListener {
		
		/**
		 * Called to notify that setup is complete.
		 */
		public void onSetupFinished();
		
		/**
		 * Called to notify that setup failed.
		 * 
		 * @param errorCodeException The result of the setup process.
		 */
		public void onSetupFailed(ErrorCodeException errorCodeException);
		
	}
	
	/**
	 * Starts the setup process. This will start up the setup process asynchronously. You will be notified through the
	 * listener when the setup process is complete. This method is safe to call from a UI thread.
	 * 
	 * @param listener The listener to notify when the setup process is complete.
	 */
	public void startSetup(final InAppBillingSetupListener listener) {
		// If already set up, can't do it again.
		checkNotDisposed();
		if (setupDone) {
			throw new UnexpectedException("InAppBillingClient is already set up.");
		}
		
		// Connection to IAB service
		LOGGER.debug("Starting in-app billing setup.");
		serviceConnection = new ServiceConnection() {
			
			@Override
			public void onServiceDisconnected(ComponentName name) {
				LOGGER.debug("Billing service disconnected.");
				service = null;
			}
			
			@Override
			public void onServiceConnected(ComponentName name, IBinder binder) {
				if (disposed) {
					return;
				}
				LOGGER.debug("Billing service connected.");
				service = IInAppBillingService.Stub.asInterface(binder);
				String packageName = context.getPackageName();
				try {
					LOGGER.debug("Checking in-app billing " + IN_APP_BILLING_API_VERSION + " support for item type "
							+ ITEM_TYPE_INAPP);
					int response = service.isBillingSupported(IN_APP_BILLING_API_VERSION, packageName, ITEM_TYPE_INAPP);
					ErrorCode inAppBillingErrorCode = InAppBillingErrorCode.findByErrorResponseCode(response);
					if (inAppBillingErrorCode == null) {
						
						LOGGER.debug("In-app billing supported for item type " + ITEM_TYPE_INAPP);
						
						LOGGER.debug("Checking in-app billing " + IN_APP_BILLING_API_VERSION
								+ " support for item type " + ITEM_TYPE_SUBS);
						response = service.isBillingSupported(IN_APP_BILLING_API_VERSION, packageName, ITEM_TYPE_SUBS);
						inAppBillingErrorCode = InAppBillingErrorCode.findByErrorResponseCode(response);
						if (inAppBillingErrorCode == null) {
							LOGGER.debug("In-app billing supported for item type " + ITEM_TYPE_SUBS);
							subscriptionsSupported = true;
						} else {
							LOGGER.warn("Subscriptions NOT AVAILABLE. InAppBillingErrorCode: " + inAppBillingErrorCode);
						}
						
						setupDone = true;
						if (listener != null) {
							listener.onSetupFinished();
						}
						
					} else {
						if (listener != null) {
							listener.onSetupFailed(inAppBillingErrorCode.newErrorCodeException());
						}
						// if in-app purchases aren't supported, neither are subscriptions.
						subscriptionsSupported = false;
					}
				} catch (RemoteException e) {
					if (listener != null) {
						listener.onSetupFailed(InAppBillingErrorCode.REMOTE_EXCEPTION.newErrorCodeException(
							"Remote exception while starting setup.", e));
					}
					return;
				}
			}
		};
		
		Intent serviceIntent = new Intent("com.android.vending.billing.InAppBillingService.BIND");
		serviceIntent.setPackage("com.android.vending");
		List<ResolveInfo> resolveInfos = context.getPackageManager().queryIntentServices(serviceIntent, 0);
		if (CollectionUtils.isNotEmpty(resolveInfos)) {
			// service available to handle that Intent
			context.bindService(serviceIntent, serviceConnection, Context.BIND_AUTO_CREATE);
		} else {
			// no service available to handle that Intent
			if (listener != null) {
				listener.onSetupFailed(InAppBillingErrorCode.BILLING_UNAVAILABLE.newErrorCodeException());
			}
		}
	}
	
	/**
	 * Callback that notifies when a purchase is finished.
	 */
	public interface InAppBillingPurchaseFinishedListener {
		
		/**
		 * Called to notify that an in-app purchase finished.
		 * 
		 * @param purchase The purchase information (null if purchase failed)
		 */
		public void onIabPurchaseFinished(Purchase purchase);
		
		/**
		 * Called to notify that an in-app purchase failed.
		 * 
		 * @param errorCodeException The result of the purchase.
		 */
		public void onIabPurchaseFailed(ErrorCodeException errorCodeException);
	}
	
	public void launchInAppPurchaseFlow(Activity activity, String sku, InAppBillingPurchaseFinishedListener listener,
			String extraData) {
		launchPurchaseFlow(activity, sku, ITEM_TYPE_INAPP, PURCHASE_REQUEST_CODE, listener, extraData);
	}
	
	public void launchSubscriptionPurchaseFlow(Activity act, String sku, int requestCode,
			InAppBillingPurchaseFinishedListener listener, String extraData) {
		launchPurchaseFlow(act, sku, ITEM_TYPE_SUBS, PURCHASE_REQUEST_CODE, listener, extraData);
	}
	
	/**
	 * Initiate the UI flow for an in-app purchase. Call this method to initiate an in-app purchase, which will involve
	 * bringing up the Google Play screen. The calling activity will be paused while the user interacts with Google
	 * Play, and the result will be delivered via the activity's {@link android.app.Activity#onActivityResult} method,
	 * at which point you must call this object's {@link #handleActivityResult} method to continue the purchase flow.
	 * This method MUST be called from the UI thread of the Activity.
	 * 
	 * @param activity The calling activity.
	 * @param sku The sku of the item to purchase.
	 * @param itemType indicates if it's a product or a subscription (ITEM_TYPE_INAPP or ITEM_TYPE_SUBS)
	 * @param requestCode A request code (to differentiate from other responses -- as in
	 *            {@link android.app.Activity#startActivityForResult}).
	 * @param listener The listener to notify when the purchase process finishes
	 * @param extraData Extra data (developer payload), which will be returned with the purchase data when the purchase
	 *            completes. This extra data will be permanently bound to that purchase and will always be returned when
	 *            the purchase is queried.
	 */
	private void launchPurchaseFlow(Activity activity, String sku, String itemType, int requestCode,
			InAppBillingPurchaseFinishedListener listener, String extraData) {
		checkNotDisposed();
		checkSetupDone("launchPurchaseFlow");
		if (flagStartAsync("launchPurchaseFlow")) {
			
			if (itemType.equals(ITEM_TYPE_SUBS) && !subscriptionsSupported) {
				flagEndAsync();
				if (listener != null) {
					listener.onIabPurchaseFailed(InAppBillingErrorCode.SUBSCRIPTIONS_NOT_AVAILABLE.newErrorCodeException());
				}
				return;
			}
			
			try {
				LOGGER.debug("Constructing buy intent for " + sku + ", item type: " + itemType);
				Bundle buyIntentBundle = service.getBuyIntent(IN_APP_BILLING_API_VERSION, context.getPackageName(),
					sku, itemType, extraData);
				InAppBillingErrorCode inAppBillingErrorCode = getResponseCode(buyIntentBundle);
				if (inAppBillingErrorCode != null) {
					flagEndAsync();
					if (listener != null) {
						listener.onIabPurchaseFailed(inAppBillingErrorCode.newErrorCodeException());
					}
					return;
				}
				
				PendingIntent pendingIntent = buyIntentBundle.getParcelable(RESPONSE_BUY_INTENT);
				LOGGER.debug("Launching buy intent for " + sku + ". Request code: " + requestCode);
				this.requestCode = requestCode;
				purchaseListener = listener;
				purchasingItemType = itemType;
				activity.startIntentSenderForResult(pendingIntent.getIntentSender(), requestCode, new Intent(), 0, 0, 0);
			} catch (SendIntentException e) {
				flagEndAsync();
				
				if (listener != null) {
					listener.onIabPurchaseFailed(InAppBillingErrorCode.SEND_INTENT_FAILED.newErrorCodeException(e));
				}
			} catch (RemoteException e) {
				flagEndAsync();
				
				if (listener != null) {
					listener.onIabPurchaseFailed(InAppBillingErrorCode.REMOTE_EXCEPTION.newErrorCodeException(e));
				}
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
		if (this.requestCode != requestCode) {
			return false;
		}
		
		checkNotDisposed();
		checkSetupDone("handleActivityResult");
		
		// end of async purchase operation that started on launchPurchaseFlow
		flagEndAsync();
		
		if (data == null) {
			if (purchaseListener != null) {
				purchaseListener.onIabPurchaseFailed(InAppBillingErrorCode.BAD_RESPONSE.newErrorCodeException("Null data on activity result."));
			}
			return true;
		}
		
		InAppBillingErrorCode inAppBillingErrorCode = getResponseCode(data.getExtras());
		if ((resultCode == Activity.RESULT_OK) && (inAppBillingErrorCode == null)) {
			
			String purchaseData = data.getStringExtra(RESPONSE_INAPP_PURCHASE_DATA);
			if (purchaseData == null) {
				if (purchaseListener != null) {
					purchaseListener.onIabPurchaseFailed(InAppBillingErrorCode.MISSING_PURCHASE_DATA.newErrorCodeException("PurchaseData is null. Extras: "
							+ data.getExtras().toString()));
				}
				return true;
			}
			
			String dataSignature = data.getStringExtra(RESPONSE_INAPP_SIGNATURE);
			if (dataSignature == null) {
				if (purchaseListener != null) {
					purchaseListener.onIabPurchaseFailed(InAppBillingErrorCode.MISSING_DATA_SIGNATURE.newErrorCodeException("DataSignature is null. Extras: "
							+ data.getExtras().toString()));
				}
				return true;
			}
			
			LOGGER.debug("Successful resultcode from purchase activity.");
			LOGGER.debug("Purchase data: " + purchaseData);
			LOGGER.debug("Data signature: " + dataSignature);
			LOGGER.debug("Extras: " + data.getExtras());
			LOGGER.debug("Expected item type: " + purchasingItemType);
			
			Purchase purchase = null;
			try {
				purchase = new Purchase(purchasingItemType, purchaseData, dataSignature);
				
				// Verify signature
				if (Security.verifyPurchase(signatureBase64, purchaseData, dataSignature)) {
					LOGGER.debug("Purchase signature successfully verified.");
					if (purchaseListener != null) {
						purchaseListener.onIabPurchaseFinished(purchase);
					}
				} else {
					if (purchaseListener != null) {
						purchaseListener.onIabPurchaseFailed(InAppBillingErrorCode.VERIFICATION_FAILED.newErrorCodeException("Purchase signature verification FAILED for sku "
								+ purchase.getSku()));
					}
				}
				
			} catch (JSONException e) {
				if (purchaseListener != null) {
					purchaseListener.onIabPurchaseFailed(InAppBillingErrorCode.BAD_PURCHASE_DATA.newErrorCodeException(e));
				}
			}
			
		} else if (resultCode == Activity.RESULT_OK) {
			if (purchaseListener != null) {
				purchaseListener.onIabPurchaseFailed(inAppBillingErrorCode.newErrorCodeException("Result code was OK but in-app billing response was not OK"));
			}
		} else if (resultCode == Activity.RESULT_CANCELED) {
			if (purchaseListener != null) {
				if (inAppBillingErrorCode != null) {
					purchaseListener.onIabPurchaseFailed(inAppBillingErrorCode.newErrorCodeException("Purchase Failed. Result code: "
							+ Integer.toString(resultCode)));
				} else {
					purchaseListener.onIabPurchaseFailed(InAppBillingErrorCode.USER_CANCELED.newErrorCodeException("Purchase canceled. Result code: "
							+ Integer.toString(resultCode)));
				}
			}
		} else {
			if (purchaseListener != null) {
				if (inAppBillingErrorCode != null) {
					purchaseListener.onIabPurchaseFailed(inAppBillingErrorCode.newErrorCodeException("Purchase Failed. Result code: "
							+ Integer.toString(resultCode)));
				} else {
					purchaseListener.onIabPurchaseFailed(InAppBillingErrorCode.UNKNOWN_PURCHASE_RESPONSE.newErrorCodeException("Purchase failed. Result code: "
							+ Integer.toString(resultCode)));
				}
			}
		}
		return true;
	}
	
	/**
	 * Listener that notifies when an inventory query operation completes.
	 */
	public interface QueryInventoryFinishedListener {
		
		/**
		 * Called to notify that an inventory query operation completed.
		 * 
		 * @param inventory The inventory.
		 */
		public void onQueryInventoryFinished(Inventory inventory);
		
		/**
		 * Called to notify that an inventory query operation failed.
		 * 
		 * @param errorCodeException The result of the operation.
		 */
		public void onQueryInventoryFailed(ErrorCodeException errorCodeException);
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
		if (flagStartAsync("queryInventoryAsync")) {
			ExecutorUtils.execute(new SafeRunnable() {
				
				@Override
				public void doRun() {
					ErrorCodeException errorCodeException = null;
					Inventory inventory = null;
					try {
						inventory = queryInventory(querySkuDetails, moreSkus);
					} catch (ErrorCodeException e) {
						errorCodeException = e;
					}
					
					flagEndAsync();
					
					final ErrorCodeException errorCodeExceptionFinal = errorCodeException;
					final Inventory inventoryFinal = inventory;
					if (!disposed && (listener != null)) {
						handler.post(new Runnable() {
							
							@Override
							public void run() {
								if (errorCodeExceptionFinal == null) {
									listener.onQueryInventoryFinished(inventoryFinal);
								} else {
									listener.onQueryInventoryFailed(errorCodeExceptionFinal);
								}
								
							}
						});
					}
				}
			});
		}
	}
	
	private Inventory queryInventory(boolean querySkuDetails, List<String> moreSkus) throws ErrorCodeException {
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
	 * @throws ErrorCodeException if a problem occurs while refreshing the inventory.
	 */
	private Inventory queryInventory(boolean querySkuDetails, List<String> moreItemSkus, List<String> moreSubsSkus)
			throws ErrorCodeException {
		Inventory inventory = null;
		if (!disposed && setupDone) {
			inventory = new Inventory();
			queryPurchases(inventory, ITEM_TYPE_INAPP);
			
			if (querySkuDetails) {
				querySkuDetails(ITEM_TYPE_INAPP, inventory, moreItemSkus);
			}
			
			// if subscriptions are supported, then also query for subscriptions
			if (subscriptionsSupported) {
				queryPurchases(inventory, ITEM_TYPE_SUBS);
				
				if (querySkuDetails) {
					querySkuDetails(ITEM_TYPE_SUBS, inventory, moreItemSkus);
				}
			}
		} else {
			LOGGER.warn("Client disposed. Not queried inventary");
		}
		
		return inventory;
	}
	
	private void queryPurchases(Inventory inventory, String itemType) throws ErrorCodeException {
		// Query purchases
		LOGGER.debug("Querying owned items, item type: " + itemType);
		String continueToken = null;
		
		try {
			do {
				LOGGER.debug("Calling getPurchases with continuation token: " + continueToken);
				Bundle ownedItems;
				ownedItems = service.getPurchases(IN_APP_BILLING_API_VERSION, context.getPackageName(), itemType,
					continueToken);
				
				InAppBillingErrorCode inAppBillingErrorCode = getResponseCode(ownedItems);
				if (inAppBillingErrorCode != null) {
					throw inAppBillingErrorCode.newErrorCodeException("getPurchases() failed querying " + itemType);
				}
				
				List<String> ownedSkus = ownedItems.getStringArrayList(RESPONSE_INAPP_ITEM_LIST);
				if (ownedSkus == null) {
					throw InAppBillingErrorCode.BAD_RESPONSE.newErrorCodeException("Missing purchase item list from getPurchases()");
				}
				
				List<String> purchaseDataList = ownedItems.getStringArrayList(RESPONSE_INAPP_PURCHASE_DATA_LIST);
				if (purchaseDataList == null) {
					throw InAppBillingErrorCode.MISSING_PURCHASE_DATA.newErrorCodeException("Missing purchase data list from getPurchases()");
				}
				
				List<String> signatureList = ownedItems.getStringArrayList(RESPONSE_INAPP_SIGNATURE_LIST);
				if (signatureList == null) {
					throw InAppBillingErrorCode.MISSING_DATA_SIGNATURE.newErrorCodeException("Missing data signature list from getPurchases()");
				}
				
				for (int i = 0; i < purchaseDataList.size(); ++i) {
					String purchaseData = purchaseDataList.get(i);
					String signature = signatureList.get(i);
					String sku = ownedSkus.get(i);
					if (Security.verifyPurchase(signatureBase64, purchaseData, signature)) {
						LOGGER.debug("Sku is owned: " + sku);
						Purchase purchase = new Purchase(itemType, purchaseData, signature);
						
						if (TextUtils.isEmpty(purchase.getToken())) {
							LOGGER.warn("BUG: empty/null token!");
							LOGGER.debug("Purchase data: " + purchaseData);
						}
						
						// Record ownership and token
						inventory.addPurchase(purchase);
					} else {
						LOGGER.warn("Purchase signature verification failed. Sku is ignored: " + sku);
						AbstractApplication.get().getExceptionHandler().logHandledException(
							InAppBillingErrorCode.VERIFICATION_FAILED.newErrorCodeException("Purchase signature verification failed. Purchase data: ["
									+ purchaseData + "]. Signature: " + signature));
					}
				}
				
				continueToken = ownedItems.getString(INAPP_CONTINUATION_TOKEN);
				LOGGER.debug("Continuation token: " + continueToken);
			} while (!TextUtils.isEmpty(continueToken));
			
		} catch (RemoteException e) {
			throw InAppBillingErrorCode.REMOTE_EXCEPTION.newErrorCodeException(e);
		} catch (JSONException e) {
			throw InAppBillingErrorCode.BAD_RESPONSE.newErrorCodeException(e);
		}
	}
	
	private void querySkuDetails(String itemType, Inventory inventory, List<String> moreSkus) throws ErrorCodeException {
		
		LOGGER.debug("Querying SKU details.");
		ArrayList<String> skuList = Lists.newArrayList(inventory.getAllOwnedSkus(itemType));
		if (moreSkus != null) {
			for (String sku : moreSkus) {
				if (!skuList.contains(sku)) {
					skuList.add(sku);
				}
			}
		}
		
		if (!skuList.isEmpty()) {
			
			try {
				Bundle querySkus = new Bundle();
				querySkus.putStringArrayList(GET_SKU_DETAILS_ITEM_LIST, skuList);
				Bundle skuDetailsBundle = service.getSkuDetails(IN_APP_BILLING_API_VERSION, context.getPackageName(),
					itemType, querySkus);
				
				List<String> skuDetailsList = skuDetailsBundle.getStringArrayList(RESPONSE_GET_SKU_DETAILS_LIST);
				if (skuDetailsList != null) {
					for (String each : skuDetailsList) {
						SkuDetails skuDetails = new SkuDetails(itemType, each);
						LOGGER.debug("Got sku details: " + skuDetails);
						inventory.addSkuDetails(skuDetails);
					}
				} else {
					InAppBillingErrorCode inAppBillingErrorCode = getResponseCode(skuDetailsBundle);
					if (inAppBillingErrorCode != null) {
						throw inAppBillingErrorCode.newErrorCodeException("getSkuDetails() failed querying " + itemType);
					} else {
						throw InAppBillingErrorCode.BAD_RESPONSE.newErrorCodeException("getSkuDetails() returned a bundle with neither an error nor a detail list.");
					}
				}
			} catch (RemoteException e) {
				throw InAppBillingErrorCode.REMOTE_EXCEPTION.newErrorCodeException(e);
			} catch (JSONException e) {
				throw InAppBillingErrorCode.BAD_RESPONSE.newErrorCodeException(e);
			}
		}
	}
	
	/**
	 * Callback that notifies when a consumption operation finishes.
	 */
	public interface OnConsumeFinishedListener {
		
		/**
		 * Called to notify that a consumption has finished.
		 * 
		 * @param purchase The purchase that was (or was to be) consumed.
		 */
		public void onConsumeFinished(Purchase purchase);
		
		/**
		 * Called to notify that a consumption has failed.
		 * 
		 * @param errorCodeException The result of the consumption operation.
		 */
		public void onConsumeFailed(ErrorCodeException errorCodeException);
	}
	
	/**
	 * Asynchronous wrapper to item consumption. Works like {@link #consume}, but performs the consumption in the
	 * background and notifies completion through the provided listener. This method is safe to call from a UI thread.
	 * 
	 * @param purchase The purchase to be consumed.
	 * @param listener The listener to notify when the consumption operation finishes.
	 */
	public void consumeAsync(final Purchase purchase, final OnConsumeFinishedListener listener) {
		final Handler handler = new Handler();
		if (flagStartAsync("consumeAsyncInternal")) {
			ExecutorUtils.execute(new SafeRunnable() {
				
				@Override
				public void doRun() {
					ErrorCodeException errorCodeException = null;
					try {
						consume(purchase);
					} catch (ErrorCodeException e) {
						errorCodeException = e;
					}
					
					flagEndAsync();
					
					final ErrorCodeException errorCodeExceptionFinal = errorCodeException;
					if (!disposed && (listener != null)) {
						handler.post(new Runnable() {
							
							@Override
							public void run() {
								if (errorCodeExceptionFinal == null) {
									listener.onConsumeFinished(purchase);
								} else {
									listener.onConsumeFailed(errorCodeExceptionFinal);
								}
								
							}
						});
					}
				}
			});
		}
	}
	
	/**
	 * Consumes a given in-app product. Consuming can only be done on an item that's owned, and as a result of
	 * consumption, the user will no longer own it. This method may block or take long to return. Do not call from the
	 * UI thread. For that, see {@link #consumeAsync}.
	 * 
	 * @param purchase The {@link Purchase} that represents the item to consume.
	 */
	private void consume(Purchase purchase) throws ErrorCodeException {
		
		if (purchase.getItemType().equals(ITEM_TYPE_INAPP)) {
			try {
				String token = purchase.getToken();
				String sku = purchase.getSku();
				if ((token == null) || token.equals("")) {
					throw InAppBillingErrorCode.MISSING_TOKEN.newErrorCodeException("Can't consume " + sku
							+ ". No token.");
				}
				
				if (!disposed) {
					LOGGER.debug("Consuming sku: " + sku + ", token: " + token);
					int response = service.consumePurchase(IN_APP_BILLING_API_VERSION, context.getPackageName(), token);
					InAppBillingErrorCode inAppBillingErrorCode = InAppBillingErrorCode.findByErrorResponseCode(response);
					if (inAppBillingErrorCode == null) {
						LOGGER.debug("Successfully consumed sku: " + sku);
					} else {
						throw inAppBillingErrorCode.newErrorCodeException("Error consuming consuming sku " + sku);
					}
				} else {
					LOGGER.warn("Client disposed. Not consuming sku: " + sku + ", token: " + token);
				}
			} catch (RemoteException e) {
				throw InAppBillingErrorCode.REMOTE_EXCEPTION.newErrorCodeException();
			}
		} else {
			throw InAppBillingErrorCode.INVALID_CONSUMPTION.newErrorCodeException("Items of type '"
					+ purchase.getItemType() + "' can't be consumed.");
		}
	}
	
	/**
	 * Dispose of object, releasing resources. It's very important to call this method when you are done with this
	 * object. It will release any resources used by it such as service connections. Naturally, once the object is
	 * disposed of, it can't be used again.
	 */
	public void dispose() {
		LOGGER.debug("Disposing.");
		setupDone = false;
		if (serviceConnection != null) {
			LOGGER.debug("Unbinding from service.");
			if (context != null) {
				try {
					context.unbindService(serviceConnection);
				} catch (RuntimeException e) {
					LOGGER.warn("Error on unbinding", e);
				}
			}
		}
		disposed = true;
		context = null;
		serviceConnection = null;
		service = null;
		purchaseListener = null;
	}
	
	private Boolean flagStartAsync(String operation) {
		if (asyncInProgress) {
			AbstractApplication.get().getExceptionHandler().logWarningException(
				"Can't start async operation (consumeAsyncInternal) because another (" + asyncOperation
						+ ") is in progress.");
			return false;
		}
		asyncOperation = operation;
		asyncInProgress = true;
		LOGGER.debug("Starting async operation: " + operation);
		return true;
	}
	
	private void flagEndAsync() {
		LOGGER.debug("Ending async operation: " + asyncOperation);
		asyncOperation = null;
		asyncInProgress = false;
	}
	
	// Checks that setup was done; if not, throws an exception.
	private void checkSetupDone(String operation) {
		if (!setupDone) {
			throw new UnexpectedException("InAppBillingClient is not set up. Can't perform operation: " + operation);
		}
	}
	
	// Workaround to bug where sometimes response codes come as Long instead of Integer
	private InAppBillingErrorCode getResponseCode(Bundle bundle) {
		Object responseCode = bundle.get(RESPONSE_CODE);
		if (responseCode == null) {
			LOGGER.debug("Bundle/Intent with null response code, assuming OK (known issue)");
			return null;
		} else if (responseCode instanceof Integer) {
			return InAppBillingErrorCode.findByErrorResponseCode(((Integer)responseCode).intValue());
		} else if (responseCode instanceof Long) {
			return InAppBillingErrorCode.findByErrorResponseCode((int)((Long)responseCode).longValue());
		} else {
			throw new UnexpectedException("Unexpected type for bundle response code: "
					+ responseCode.getClass().getName());
		}
	}
	
	private void checkNotDisposed() {
		if (disposed) {
			throw new UnexpectedException("InAppBillingClient was disposed of, so it cannot be used.");
		}
	}
	
	/**
	 * @return whether subscriptions are supported.
	 */
	public boolean subscriptionsSupported() {
		checkNotDisposed();
		return subscriptionsSupported;
	}
}
