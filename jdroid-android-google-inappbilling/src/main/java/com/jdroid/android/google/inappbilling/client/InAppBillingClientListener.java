package com.jdroid.android.google.inappbilling.client;

import android.support.annotation.MainThread;

import com.jdroid.java.exception.ErrorCodeException;

public interface InAppBillingClientListener {
	
	/**
	 * Called to notify that setup is complete.
	 */
	@MainThread
	public void onSetupFinished();
	
	/**
	 * Called to notify that setup failed.
	 * 
	 * @param errorCodeException The result of the setup process.
	 */
	@MainThread
	public void onSetupFailed(ErrorCodeException errorCodeException);
	
	/**
	 * Called to notify that an inventory query operation completed.
	 * 
	 * @param inventory The inventory.
	 */
	@MainThread
	public void onQueryInventoryFinished(Inventory inventory);
	
	/**
	 * Called to notify that an inventory query operation failed.
	 * 
	 * @param errorCodeException The result of the operation.
	 */
	@MainThread
	public void onQueryInventoryFailed(ErrorCodeException errorCodeException);
	
	/**
	 * Called to notify that an in-app purchase finished.
	 * 
	 * @param product The {@link Product} purchased
	 */
	@MainThread
	public void onPurchaseFinished(Product product);
	
	/**
	 * Called to notify that an in-app purchase failed.
	 * 
	 * @param errorCodeException The result of the purchase.
	 */
	@MainThread
	public void onPurchaseFailed(ErrorCodeException errorCodeException);
	
	/**
	 * Called to notify that a consumption has finished.
	 * 
	 * @param product The {@link Product} that was (or was to be) consumed.
	 */
	@MainThread
	public void onConsumeFinished(Product product);
	
	/**
	 * Called to notify that a consumption has failed.
	 * 
	 * @param errorCodeException The result of the consumption operation.
	 */
	@MainThread
	public void onConsumeFailed(ErrorCodeException errorCodeException);
	
	@MainThread
	public void onProvideProduct(Product product);
	
}
