package com.jdroid.android.google.inappbilling;

import android.os.Bundle;

import com.jdroid.android.activity.AbstractFragmentActivity;
import com.jdroid.android.activity.ActivityDelegate;

public class InAppBillingActivityDelegate extends ActivityDelegate {

	public InAppBillingActivityDelegate(AbstractFragmentActivity activity) {
		super(activity);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		InAppBillingHelper.onCreate(getActivity(), savedInstanceState);
	}
}
