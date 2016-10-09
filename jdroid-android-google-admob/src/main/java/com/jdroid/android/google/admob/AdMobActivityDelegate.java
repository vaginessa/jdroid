package com.jdroid.android.google.admob;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.ViewGroup;

import com.google.android.gms.ads.MobileAds;
import com.jdroid.android.activity.AbstractFragmentActivity;
import com.jdroid.android.activity.ActivityDelegate;
import com.jdroid.android.google.admob.helpers.AdHelper;
import com.jdroid.android.google.admob.helpers.AdViewHelper;
import com.jdroid.android.google.admob.helpers.BaseAdViewHelper;
import com.jdroid.android.google.admob.helpers.InterstitialAdHelper;

public class AdMobActivityDelegate extends ActivityDelegate {

	private static Boolean initialized = false;

	private BaseAdViewHelper baseAdViewHelper;
	private InterstitialAdHelper interstitialAdHelper;

	public AdMobActivityDelegate(AbstractFragmentActivity activity) {
		super(activity);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		if (AdMobAppModule.get().getAdMobAppContext().areAdsEnabled()) {

			if (!initialized) {
				MobileAds.initialize(getActivity().getApplicationContext(), AdMobAppModule.get().getAdMobAppContext().getAdMobAppId());
				initialized = true;
			}

			baseAdViewHelper = createBaseAdViewHelper();
			if (baseAdViewHelper != null) {
				initBaseAdViewHelper(baseAdViewHelper);
				baseAdViewHelper.loadAd(getActivity(), (ViewGroup)(getActivity().findViewById(R.id.adViewContainer)));
			}

			interstitialAdHelper = createInterstitialAdHelper();
			if (interstitialAdHelper != null) {
				initInterstitialAdHelper(interstitialAdHelper);
				interstitialAdHelper.loadAd(getActivity(), null);
			}

		}
	}

	@Override
	public void onResume() {
		if (baseAdViewHelper != null) {
			baseAdViewHelper.onResume();
		}
	}

	@Override
	public void onBeforePause() {
		if (baseAdViewHelper != null) {
			baseAdViewHelper.onPause();
		}
	}

	@Override
	public void onBeforeDestroy() {
		if (baseAdViewHelper != null) {
			baseAdViewHelper.onDestroy();
		}
	}

	@Nullable
	public BaseAdViewHelper createBaseAdViewHelper() {
		return new AdViewHelper();
	}

	@Nullable
	public InterstitialAdHelper createInterstitialAdHelper() {
		return null;
	}

	public void initBaseAdViewHelper(BaseAdViewHelper adHelper) {
		// Do nothing
	}

	public void initInterstitialAdHelper(InterstitialAdHelper adHelper) {
		// Do nothing
	}

	@Nullable
	public BaseAdViewHelper getBaseAdViewHelper() {
		return baseAdViewHelper;
	}

	@Nullable
	public InterstitialAdHelper getInterstitialAdHelper() {
		return interstitialAdHelper;
	}
}
