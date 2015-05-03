package com.jdroid.sample.android.hero;

import android.support.v4.app.Fragment;

import com.jdroid.android.activity.FragmentContainerActivity;

public class HeroActivity extends FragmentContainerActivity {

	/**
	 * @see FragmentContainerActivity#getFragmentClass()
	 */
	@Override
	protected Class<? extends Fragment> getFragmentClass() {
		return HeroFragment.class;
	}
}