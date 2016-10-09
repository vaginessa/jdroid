package com.jdroid.android.sample.ui.ads;

import android.support.annotation.Nullable;

import com.google.android.gms.ads.AdSize;
import com.jdroid.android.application.AppModule;
import com.jdroid.android.fragment.AbstractFragment;
import com.jdroid.android.fragment.FragmentDelegate;
import com.jdroid.android.google.admob.helpers.AdHelper;
import com.jdroid.android.google.admob.helpers.BaseAdViewHelper;
import com.jdroid.android.google.admob.AdMobAppModule;
import com.jdroid.android.google.admob.AdMobFragmentDelegate;
import com.jdroid.android.google.admob.helpers.NativeExpressAdViewHelper;
import com.jdroid.android.sample.R;
import com.jdroid.android.sample.application.AndroidAppContext;

public class FragmentNativeAdExpressFragment extends AbstractFragment {
	
	@Override
	public Integer getContentFragmentLayout() {
		return R.layout.empty_fragment;
	}
	
	@Override
	public FragmentDelegate createFragmentDelegate(AppModule appModule) {
		if (appModule instanceof AdMobAppModule) {
			return new AdMobFragmentDelegate(this) {

				@Nullable
				@Override
				public AdHelper createAdHelper() {
					return new NativeExpressAdViewHelper();
				}

				@Override
				public void initAdHelper(AdHelper adHelper) {
					BaseAdViewHelper baseAdViewHelper = (BaseAdViewHelper)adHelper;
					baseAdViewHelper.setAdSize(new AdSize(AdSize.FULL_WIDTH, 80));
					baseAdViewHelper.setAdUnitId(AndroidAppContext.SAMPLE_NATIVE_AD_EXPRESS_AD_UNIT_ID);
				}
			};
		} else {
			return super.createFragmentDelegate(appModule);
		}
	}
}
