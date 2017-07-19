package com.jdroid.android.sample.google.inappbilling;

import com.jdroid.android.google.inappbilling.client.InAppBillingBroadcastListener;
import com.jdroid.java.utils.LoggerUtils;

public class SampleInAppBillingBroadcastListener implements InAppBillingBroadcastListener {
	
	@Override
	public void onPurchasesUpdated() {
		LoggerUtils.getLogger(SampleInAppBillingBroadcastListener.class).debug(SampleInAppBillingBroadcastListener.class.getSimpleName() + " executed");
	}
}
