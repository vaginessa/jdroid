package com.jdroid.android.google.admob;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.ViewGroup;

import com.google.android.gms.ads.MobileAds;
import com.jdroid.android.activity.AbstractFragmentActivity;
import com.jdroid.android.activity.ActivityDelegate;
import com.jdroid.android.google.admob.helpers.AdHelper;
import com.jdroid.android.google.admob.helpers.AdMobInterstitialAdHelper;
import com.jdroid.android.google.admob.helpers.AdViewHelper;

public class AdMobActivityDelegate extends ActivityDelegate {

	private static Boolean initialized = false;

	private AdHelper bannerAdHelper;
	private AdMobInterstitialAdHelper interstitialAdHelper;

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

			bannerAdHelper = createBannerAdHelper();
			if (bannerAdHelper != null) {
				initBannerAdHelper(bannerAdHelper);
				bannerAdHelper.loadAd(getActivity(), (ViewGroup)(getActivity().findViewById(R.id.adViewContainer)));
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
		if (bannerAdHelper != null) {
			bannerAdHelper.onResume();
		}
		if (interstitialAdHelper != null) {
			interstitialAdHelper.onResume();
		}
	}

	@Override
	public void onBeforePause() {
		if (bannerAdHelper != null) {
			bannerAdHelper.onPause();
		}
		if (interstitialAdHelper != null) {
			interstitialAdHelper.onPause();
		}
	}

	@Override
	public void onBeforeDestroy() {
		if (bannerAdHelper != null) {
			bannerAdHelper.onDestroy();
		}
		if (interstitialAdHelper != null) {
			interstitialAdHelper.onDestroy();
		}
	}

	@Nullable
	public AdHelper createBannerAdHelper() {
		return new AdViewHelper();
	}

	@Nullable
	public AdMobInterstitialAdHelper createInterstitialAdHelper() {
		return null;
	}

	public void initBannerAdHelper(AdHelper adHelper) {
		// Do nothing
	}

	public void initInterstitialAdHelper(AdMobInterstitialAdHelper adHelper) {
		// Do nothing
	}

	@Nullable
	public AdHelper getBannerAdHelper() {
		return bannerAdHelper;
	}

	@Nullable
	public AdMobInterstitialAdHelper getInterstitialAdHelper() {
		return interstitialAdHelper;
	}
}
