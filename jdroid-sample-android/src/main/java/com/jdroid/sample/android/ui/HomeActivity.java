package com.jdroid.sample.android.ui;

import android.support.v4.app.Fragment;
import com.jdroid.android.activity.FragmentContainerActivity;

public class HomeActivity extends FragmentContainerActivity {
	
	/**
	 * @see com.jdroid.android.activity.FragmentContainerActivity#getFragmentClass()
	 */
	@Override
	protected Class<? extends Fragment> getFragmentClass() {
		return HomeFragment.class;
	}
	
	/**
	 * @see com.jdroid.android.activity.AbstractFragmentActivity#isNavDrawerTopLevelView()
	 */
	@Override
	public Boolean isNavDrawerTopLevelView() {
		return true;
	}
}
