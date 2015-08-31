package com.jdroid.android.sample.ui.ads;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.View.OnClickListener;

import com.google.android.gms.ads.AdSize;
import com.jdroid.android.activity.ActivityLauncher;
import com.jdroid.android.ad.AdHelper;
import com.jdroid.android.ad.AdMobAdHelper;
import com.jdroid.android.fragment.AbstractFragment;
import com.jdroid.android.sample.R;
import com.jdroid.android.sample.application.AndroidAppContext;

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
		
		findView(R.id.displayInterstitial).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				getActivityIf().getAdHelper().displayInterstitial(false);
			}
		});

		findView(R.id.houseAds).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ActivityLauncher.launchActivity(HouseAdsActivity.class);
			}
		});
	}

	@Nullable
	@Override
	public AdHelper createAdHelper() {
		AdMobAdHelper adHelper = new AdMobAdHelper();
		adHelper.setAdSize(AdSize.BANNER);
		adHelper.setBannerAdUnitId(AndroidAppContext.SAMPLE_BANNER_AD_UNIT_ID);
		return adHelper;
	}
}
