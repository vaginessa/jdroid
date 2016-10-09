package com.jdroid.android.sample.ui.ads;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.jdroid.android.activity.ActivityDelegate;
import com.jdroid.android.activity.FragmentContainerActivity;
import com.jdroid.android.application.AppModule;
import com.jdroid.android.google.admob.AdMobActivityDelegate;
import com.jdroid.android.google.admob.AdMobAppModule;
import com.jdroid.android.google.admob.helpers.AdMobInterstitialAdHelper;
import com.jdroid.android.sample.application.AndroidAppContext;

public class AdsActivity extends FragmentContainerActivity {
	
	@Override
	protected Class<? extends Fragment> getFragmentClass() {
		return AdsFragment.class;
	}

	@Override
	public ActivityDelegate createActivityDelegate(AppModule appModule) {
		if (appModule instanceof AdMobAppModule) {
			return new AdMobActivityDelegate(this) {

				@Nullable
				@Override
				public AdMobInterstitialAdHelper createInterstitialAdHelper() {
					return new AdMobInterstitialAdHelper();
				}

				@Override
				public void initInterstitialAdHelper(AdMobInterstitialAdHelper adHelper) {
					adHelper.setAdUnitId(AndroidAppContext.SAMPLE_INTERSTITIAL_AD_UNIT_ID);
				}
			};
		} else {
			return super.createActivityDelegate(appModule);
		}
	}
}
