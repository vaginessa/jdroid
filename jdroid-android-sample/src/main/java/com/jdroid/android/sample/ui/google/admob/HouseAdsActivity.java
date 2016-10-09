package com.jdroid.android.sample.ui.google.admob;

import android.support.v4.app.Fragment;

import com.jdroid.android.activity.FragmentContainerActivity;

public class HouseAdsActivity extends FragmentContainerActivity {

	@Override
	protected Class<? extends Fragment> getFragmentClass() {
		return HouseAdsFragment.class;
	}
}
