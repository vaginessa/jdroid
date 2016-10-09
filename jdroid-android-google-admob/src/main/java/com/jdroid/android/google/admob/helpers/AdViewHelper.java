package com.jdroid.android.google.admob.helpers;

import android.app.Activity;

import com.google.android.gms.ads.AdView;

public class AdViewHelper extends BaseAdViewHelper {

	@Override
	protected BaseAdViewWrapper createBaseAdViewWrapper(Activity activity) {
		return new BaseAdViewWrapper(new AdView(activity));
	}
}
