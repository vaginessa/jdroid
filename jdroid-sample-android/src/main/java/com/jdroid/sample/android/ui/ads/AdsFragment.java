package com.jdroid.sample.android.ui.ads;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.google.android.gms.ads.AdSize;
import com.jdroid.android.fragment.AbstractFragment;
import com.jdroid.sample.android.AndroidAppContext;
import com.jdroid.sample.android.R;

public class AdsFragment extends AbstractFragment {
	
	@Override
	public Integer getContentFragmentLayout() {
		return R.layout.ads_fragment;
	}
	
	/**
	 * @see com.jdroid.android.fragment.AbstractFragment#onViewCreated(android.view.View, android.os.Bundle)
	 */
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		Button showInterstitial = findView(R.id.displayInterstitial);
		showInterstitial.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				getActivityIf().displayInterstitial(false);
			}
		});
	}

	@Override
	public String getBannerAdUnitId() {
		return AndroidAppContext.SAMPLE_BANNER_AD_UNIT_ID;
	}

	/**
	 * @see com.jdroid.android.fragment.AbstractFragment#getAdSize()
	 */
	@Override
	public AdSize getAdSize() {
		return AdSize.SMART_BANNER;
	}
}
