package com.jdroid.sample.android.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;

import com.jdroid.android.AbstractApplication;
import com.jdroid.android.activity.AbstractFragmentActivity;
import com.jdroid.android.activity.FragmentContainerActivity;
import com.jdroid.android.google.gcm.AbstractGcmRegistrationService;
import com.jdroid.android.navdrawer.NavDrawer;
import com.jdroid.sample.android.gcm.AndroidGcmRegistrationService;

public class HomeActivity extends FragmentContainerActivity {

	/**
	 * @see com.jdroid.android.activity.AbstractFragmentActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (savedInstanceState == null) {
			AbstractApplication.get().getUriMapper().checkDeepLink(this);
			AbstractGcmRegistrationService.runRegistrationService(getApplicationContext(), AndroidGcmRegistrationService.class);
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
	public NavDrawer createNavDrawer(AbstractFragmentActivity activity, Boolean darkTheme, Toolbar appBar) {
		NavDrawer navDrawer = super.createNavDrawer(activity, darkTheme, appBar);
		navDrawer.setIsNavDrawerTopLevelView(true);
		return navDrawer;
	}
}
