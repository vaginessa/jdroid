package com.jdroid.android.ad;

import android.app.Activity;
import android.view.ViewGroup;

public interface AdHelper {
	
	public void onResume();

	public void onPause();

	public void onDestroy();

	public AdHelper setAdViewContainer(ViewGroup adViewContainer);

	public AdHelper setBannerAdUnitId(String bannerAdUnitId);

	public AdHelper setInterstitialAdUnitId(String interstitialAdUnitId);

	public AdHelper setIsInterstitialEnabled(Boolean isInterstitialEnabled);

	public void displayInterstitial(Boolean retryIfNotLoaded);

	public void loadBanner(Activity activity);

	public void loadInterstitial(Activity activity);

}
