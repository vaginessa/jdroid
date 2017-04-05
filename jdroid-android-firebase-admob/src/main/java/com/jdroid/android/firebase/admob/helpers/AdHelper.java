package com.jdroid.android.firebase.admob.helpers;

import android.app.Activity;
import android.view.ViewGroup;

public interface AdHelper {
	
	public AdHelper setAdUnitId(String adUnitId);

	public void loadAd(Activity activity, ViewGroup adViewContainer);
}
