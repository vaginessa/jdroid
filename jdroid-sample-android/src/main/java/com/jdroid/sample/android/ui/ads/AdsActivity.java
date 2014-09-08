package com.jdroid.sample.android.ui.ads;

import android.support.v4.app.Fragment;
import com.jdroid.android.activity.FragmentContainerActivity;

public class AdsActivity extends FragmentContainerActivity {
	
	/**
	 * @see com.jdroid.android.activity.FragmentContainerActivity#getFragmentClass()
	 */
	@Override
	protected Class<? extends Fragment> getFragmentClass() {
		return AdsFragment.class;
	}
	
	/**
	 * @see com.jdroid.android.activity.AbstractFragmentActivity#isInterstitialEnabled()
	 */
	@Override
	public Boolean isInterstitialEnabled() {
		return true;
	}
}
