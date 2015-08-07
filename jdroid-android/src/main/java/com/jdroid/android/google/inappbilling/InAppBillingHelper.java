package com.jdroid.android.google.inappbilling;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public class InAppBillingHelper {

	private static Boolean inAppBillingLoaded = false;

	public static void onCreate(FragmentActivity activity, Bundle savedInstanceState) {

		if ((savedInstanceState == null) && !inAppBillingLoaded) {
			InAppBillingHelperFragment.add(activity, InAppBillingHelperFragment.class, true, null);
			inAppBillingLoaded = true;
		}
	}
}
