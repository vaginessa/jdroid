package com.jdroid.android.sample.ui.ads;

import android.support.v4.app.Fragment;

import com.jdroid.android.activity.FragmentContainerActivity;

public class FragmentBannerActivity extends FragmentContainerActivity {
	
	@Override
	protected Class<? extends Fragment> getFragmentClass() {
		return FragmentBannerFragment.class;
	}
}
