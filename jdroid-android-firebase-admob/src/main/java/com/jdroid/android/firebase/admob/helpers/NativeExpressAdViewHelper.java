package com.jdroid.android.firebase.admob.helpers;

import android.app.Activity;

import com.google.android.gms.ads.NativeExpressAdView;

public class NativeExpressAdViewHelper extends BaseAdViewHelper {

	@Override
	protected BaseAdViewWrapper createBaseAdViewWrapper(Activity activity) {
		return new BaseAdViewWrapper(new NativeExpressAdView(activity));
	}
}
