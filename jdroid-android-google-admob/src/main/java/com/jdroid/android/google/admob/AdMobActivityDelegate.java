package com.jdroid.android.google.admob;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.ViewGroup;

import com.jdroid.android.activity.AbstractFragmentActivity;
import com.jdroid.android.activity.ActivityDelegate;

public class AdMobActivityDelegate extends ActivityDelegate {

	private AdHelper adHelper;

	public AdMobActivityDelegate(AbstractFragmentActivity activity) {
		super(activity);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		if (AdMobAppModule.get().getAdMobAppContext().areAdsEnabled()) {
			adHelper = createAdHelper();
			if (adHelper != null) {
				initAdHelper(adHelper);
				adHelper.loadBanner(getActivity(), (ViewGroup)(getActivity().findViewById(R.id.adViewContainer)));
				adHelper.loadInterstitial(getActivity());
			}
		}
	}

	@Override
	public void onResume() {
		if (adHelper != null) {
			adHelper.onResume();
		}
	}

	@Override
	public void onBeforePause() {
		if (adHelper != null) {
			adHelper.onPause();
		}
	}

	@Override
	public void onBeforeDestroy() {
		if (adHelper != null) {
			adHelper.onDestroy();
		}
	}

	@Nullable
	public AdHelper createAdHelper() {
		return new AdMobAdHelper();
	}

	public void initAdHelper(AdHelper adHelper) {
		// Do nothing
	}

	@Nullable
	public AdHelper getAdHelper() {
		return adHelper;
	}
}
