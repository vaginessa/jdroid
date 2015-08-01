package com.jdroid.sample.android.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;

import com.jdroid.android.AbstractApplication;
import com.jdroid.android.activity.AbstractFragmentActivity;
import com.jdroid.android.activity.FragmentContainerActivity;
import com.jdroid.android.navdrawer.NavDrawer;

public class HomeActivity extends FragmentContainerActivity {

	/**
	 * @see com.jdroid.android.activity.AbstractFragmentActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (savedInstanceState == null) {
			AbstractApplication.get().getUriMapper().checkDeepLink(this);
			// TODO Enable to make gcm work
//			AbstractGcmRegistrationService.runRegistrationService(getApplicationContext(), AndroidGcmRegistrationService.class);
		}
	}

	/**
	 * @see com.jdroid.android.activity.FragmentContainerActivity#getFragmentClass()
	 */
	@Override
	protected Class<? extends Fragment> getFragmentClass() {
		return HomeFragment.class;
	}

	@Override
	public NavDrawer createNavDrawer(AbstractFragmentActivity activity, Toolbar appBar) {
		NavDrawer navDrawer = super.createNavDrawer(activity, appBar);
		navDrawer.setIsNavDrawerTopLevelView(true);
		return navDrawer;
	}
}
