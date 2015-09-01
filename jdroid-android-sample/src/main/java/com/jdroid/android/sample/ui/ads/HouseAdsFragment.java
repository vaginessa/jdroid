package com.jdroid.android.sample.ui.ads;

import android.app.Activity;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.ads.AdSize;
import com.jdroid.android.ad.AdHelper;
import com.jdroid.android.fragment.AbstractFragment;
import com.jdroid.android.google.admob.AdMobAdHelper;
import com.jdroid.android.google.admob.HouseAdBuilder;
import com.jdroid.android.sample.R;

public class HouseAdsFragment extends AbstractFragment {

	@Override
	public Integer getContentFragmentLayout() {
		return R.layout.house_ads_fragment;
	}

	@Nullable
	@Override
	public AdHelper createAdHelper() {
		AdMobAdHelper adHelper = new AdMobAdHelper();
		adHelper.setAdSize(AdSize.BANNER);
		adHelper.setHouseAdBuilder(new HouseAdBuilder() {
			@Override
			public View build(Activity activity) {
				View view = super.build(activity);
				if (view == null) {
					view = new TextView(getActivity());
					((TextView)view).setText("House ad");
				}
				return view;
			}
		});
		return adHelper;
	}
}
