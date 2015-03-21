package com.jdroid.sample.android.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.jdroid.android.AbstractApplication;
import com.jdroid.android.activity.FragmentContainerActivity;

public class HomeActivity extends FragmentContainerActivity {

	/**
	 * @see com.jdroid.android.activity.AbstractFragmentActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (savedInstanceState == null) {
			AbstractApplication.get().getUriMapper().checkDeepLink(this);
		}
	}

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
