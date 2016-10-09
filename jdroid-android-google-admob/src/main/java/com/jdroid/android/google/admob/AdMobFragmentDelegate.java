package com.jdroid.android.google.admob;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;

import com.jdroid.android.fragment.FragmentDelegate;
import com.jdroid.android.google.admob.helpers.AdHelper;
import com.jdroid.android.google.admob.helpers.AdViewHelper;

public class AdMobFragmentDelegate extends FragmentDelegate {

	private AdHelper adHelper;

	public AdMobFragmentDelegate(Fragment fragment) {
		super(fragment);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		if (AdMobAppModule.get().getAdMobAppContext().areAdsEnabled()) {
			adHelper = createAdHelper();
			if (adHelper != null) {
				initAdHelper(adHelper);
				adHelper.loadAd(getFragment().getActivity(), (ViewGroup)(view.findViewById(R.id.adViewContainer)));
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
		return new AdViewHelper();
	}

	public void initAdHelper(AdHelper adHelper) {
		// Do nothing
	}

	@Nullable
	public AdHelper getAdHelper() {
		return adHelper;
	}
}
