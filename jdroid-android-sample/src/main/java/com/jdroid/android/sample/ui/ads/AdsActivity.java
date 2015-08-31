package com.jdroid.android.sample.ui.ads;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import com.jdroid.android.activity.FragmentContainerActivity;
import com.jdroid.android.ad.AdHelper;
import com.jdroid.android.uri.UriHandler;
import com.jdroid.android.sample.application.AndroidAppContext;

public class AdsActivity extends FragmentContainerActivity {
	
	/**
	 * @see com.jdroid.android.activity.FragmentContainerActivity#getFragmentClass()
	 */
	@Override
	protected Class<? extends Fragment> getFragmentClass() {
		return AdsFragment.class;
	}
	
	@Nullable
	@Override
	public AdHelper createAdHelper() {
		AdHelper adHelper = super.createAdHelper();
		adHelper.setInterstitialAdUnitId(AndroidAppContext.SAMPLE_INTERSTITIAL_AD_UNIT_ID);
		adHelper.setIsInterstitialEnabled(true);
		return adHelper;
	}

	@Override
	public UriHandler getUriHandler() {
		return new AdsUriHandler();
	}
}
