package com.jdroid.android.sample.ui;

import android.support.v7.widget.Toolbar;

import com.jdroid.android.activity.AbstractFragmentActivity;
import com.jdroid.android.activity.ActivityHelper;
import com.jdroid.android.navdrawer.DefaultNavDrawer;
import com.jdroid.android.navdrawer.NavDrawer;
import com.jdroid.android.navdrawer.NavDrawerHeader;
import com.jdroid.android.navdrawer.NavDrawerItem;
import com.jdroid.android.sample.R;
import com.jdroid.java.collections.Lists;
import com.jdroid.android.sample.ui.navdrawer.AndroidNavDrawerItem;

import java.util.List;

public class AndroidActivityHelper extends ActivityHelper {
	
	public AndroidActivityHelper(AbstractFragmentActivity activity) {
		super(activity);
	}

	@Override
	public Boolean isNavDrawerEnabled() {
		return true;
	}

	@Override
	public NavDrawer createNavDrawer(AbstractFragmentActivity activity, Toolbar appBar) {
		return new DefaultNavDrawer(activity, appBar) {

			@Override
			public List<NavDrawerItem> getNavDrawerItems() {
				return Lists.<NavDrawerItem>newArrayList(AndroidNavDrawerItem.values());
			}

			@Override
			protected void initNavDrawerHeader(NavDrawerHeader navDrawerHeader) {
				super.initNavDrawerHeader(navDrawerHeader);
				navDrawerHeader.setBackground(R.drawable.hero);
			}
		};
	}

	@Override
	public Boolean isGooglePlayServicesVerificationEnabled() {
		return true;
	}
}
