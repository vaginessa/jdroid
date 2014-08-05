package com.jdroid.android.ad;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;

public class HouseAdBuilder {
	
	private OnClickListener removeAdsClickListener;
	
	public View build(Activity activity) {
		View view = null;
		if (RateAppView.displayRateMe()) {
			view = new RateAppView(activity);
		} else if (RemoveAdsView.displayRemoveAdsView() && (removeAdsClickListener != null)) {
			view = new RemoveAdsView(activity);
			view.setOnClickListener(removeAdsClickListener);
		}
		return view;
	}
	
	public void setRemoveAdsClickListener(OnClickListener removeAdsClickListener) {
		this.removeAdsClickListener = removeAdsClickListener;
	}
}
