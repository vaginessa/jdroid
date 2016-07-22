package com.jdroid.android.sample.ui.ads;

import android.support.v4.app.Fragment;

import com.jdroid.android.activity.ActivityDelegate;
import com.jdroid.android.activity.FragmentContainerActivity;
import com.jdroid.android.application.AppModule;
import com.jdroid.android.google.admob.AdHelper;
import com.jdroid.android.google.admob.AdMobActivityDelegate;
import com.jdroid.android.google.admob.AdMobAdHelper;
import com.jdroid.android.google.admob.AdMobAppModule;
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
				@Override
				public void initAdHelper(AdHelper adHelper) {
					AdMobAdHelper adMobAdHelper = (AdMobAdHelper)adHelper;
					adMobAdHelper.setInterstitialAdUnitId(AndroidAppContext.SAMPLE_INTERSTITIAL_AD_UNIT_ID);
					adMobAdHelper.setIsInterstitialEnabled(true);
				}
			};
		} else {
			return super.createActivityDelegate(appModule);
		}
	}
}
