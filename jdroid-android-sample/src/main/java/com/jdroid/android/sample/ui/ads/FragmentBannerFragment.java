package com.jdroid.android.sample.ui.ads;

import com.google.android.gms.ads.AdSize;
import com.jdroid.android.google.admob.AdHelper;
import com.jdroid.android.application.AppModule;
import com.jdroid.android.fragment.AbstractFragment;
import com.jdroid.android.fragment.FragmentDelegate;
import com.jdroid.android.google.admob.AdMobAdHelper;
import com.jdroid.android.google.admob.AdMobAppModule;
import com.jdroid.android.google.admob.AdMobFragmentDelegate;
import com.jdroid.android.sample.R;
import com.jdroid.android.sample.application.AndroidAppContext;

public class FragmentBannerFragment extends AbstractFragment {
	
	@Override
	public Integer getContentFragmentLayout() {
		return R.layout.empty_fragment;
	}
	
	@Override
	public FragmentDelegate createFragmentDelegate(AppModule appModule) {
		if (appModule instanceof AdMobAppModule) {
			return new AdMobFragmentDelegate(this) {
				@Override
				public void initAdHelper(AdHelper adHelper) {
					AdMobAdHelper adMobAdHelper = (AdMobAdHelper)adHelper;
					adMobAdHelper.setAdSize(AdSize.BANNER);
					adMobAdHelper.setBannerAdUnitId(AndroidAppContext.SAMPLE_BANNER_AD_UNIT_ID);
				}
			};
		} else {
			return super.createFragmentDelegate(appModule);
		}
	}
}
