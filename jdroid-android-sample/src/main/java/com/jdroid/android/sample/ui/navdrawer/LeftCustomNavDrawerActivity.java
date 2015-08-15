package com.jdroid.android.sample.ui.navdrawer;

import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;

import com.jdroid.android.activity.AbstractFragmentActivity;
import com.jdroid.android.activity.FragmentContainerActivity;
import com.jdroid.android.navdrawer.NavDrawer;
import com.jdroid.android.sample.R;

public class LeftCustomNavDrawerActivity extends FragmentContainerActivity {

	public int getContentView() {
		return R.layout.left_nav_fragment_container_activity;
	}

	@Override
	protected Class<? extends Fragment> getFragmentClass() {
		return LeftCustomNavDrawerFragment.class;
	}

	@Override
	public NavDrawer createNavDrawer(AbstractFragmentActivity activity, Toolbar appBar) {
		return new LeftSampleNavDrawer(activity, appBar);
	}
}
