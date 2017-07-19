package com.jdroid.android.sample.ui.google.inappbilling;

import com.jdroid.android.google.inappbilling.InAppBillingAppModule;
import com.jdroid.android.google.inappbilling.InAppBillingContext;

public class AndroidInAppBillingAppModule extends InAppBillingAppModule {

	@Override
	protected InAppBillingContext createInAppBillingContext() {
		return new AndroidInAppBilllingContext();
	}
}
