package com.jdroid.sample.android.ui.ads;

import com.jdroid.android.ad.HouseAdBuilder;
import com.jdroid.android.fragment.AbstractFragment;
import com.jdroid.sample.android.R;

public class HouseAdsFragment extends AbstractFragment {

	@Override
	public Integer getContentFragmentLayout() {
		return R.layout.house_ads_fragment;
	}

	@Override
	public HouseAdBuilder getHouseAdBuilder() {
		return new HouseAdBuilder();
	}
}
