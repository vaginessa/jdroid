package com.jdroid.android.google.inappbilling.client;

/**
 * Listener interface for received broadcast messages.
 */
public interface InAppBillingBroadcastListener {
	
	// TODO Your onReceive() method should respond to the intent by calling getPurchases()
	void onPurchasesUpdated();
}
