package com.jdroid.android.sample.ui.google.admob;

import android.support.annotation.Nullable;

import com.google.android.gms.ads.AdSize;
import com.jdroid.android.application.AppModule;
import com.jdroid.android.fragment.AbstractFragment;
import com.jdroid.android.fragment.FragmentDelegate;
import com.jdroid.android.google.admob.AdMobAppModule;
import com.jdroid.android.google.admob.AdMobFragmentDelegate;
import com.jdroid.android.google.admob.helpers.BaseAdViewHelper;
import com.jdroid.android.google.admob.helpers.NativeExpressAdViewHelper;
import com.jdroid.android.sample.R;
import com.jdroid.android.sample.application.AndroidAppContext;

public class LargeNativeAdExpressFragment extends AbstractFragment {
	
	@Override
	public Integer getContentFragmentLayout() {
		return R.layout.large_native_ad_express_fragment;
	}
	
	@Override
	public FragmentDelegate createFragmentDelegate(AppModule appModule) {
		if (appModule instanceof AdMobAppModule) {
			return new AdMobFragmentDelegate(this) {

				@Override
				protected int getAdViewContainerId() {
					return R.id.adViewContainer2;
				}

				@Nullable
				@Override
				public BaseAdViewHelper createBaseAdViewHelper() {
					return new NativeExpressAdViewHelper();
				}

				@Override
				public void initBaseAdViewHelper(BaseAdViewHelper baseAdViewHelper) {
					baseAdViewHelper.setAdSize(new AdSize(AdSize.FULL_WIDTH, 250));
					baseAdViewHelper.setAdUnitId(AndroidAppContext.SAMPLE_LARGE_NATIVE_AD_EXPRESS_AD_UNIT_ID);
				}
			};
		} else {
			return super.createFragmentDelegate(appModule);
		}
	}
}
