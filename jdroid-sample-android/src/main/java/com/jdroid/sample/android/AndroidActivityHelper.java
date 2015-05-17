package com.jdroid.sample.android;

import com.jdroid.android.activity.AbstractFragmentActivity;
import com.jdroid.android.activity.ActivityHelper;
import com.jdroid.android.navdrawer.NavDrawerHeaderBuilder;
import com.jdroid.android.navdrawer.NavDrawerItem;
import com.jdroid.java.collections.Lists;
import com.jdroid.sample.android.ui.navdrawer.AndroidNavDrawerItem;

import java.util.List;

public class AndroidActivityHelper extends ActivityHelper {
	
	public AndroidActivityHelper(AbstractFragmentActivity activity) {
		super(activity);
	}
	
	/**
	 * @see com.jdroid.android.activity.ActivityHelper#isNavDrawerEnabled()
	 */
	@Override
	public Boolean isNavDrawerEnabled() {
		return true;
	}

	@Override
	protected Boolean isNavDrawerHeaderVisible() {
		return true;
	}

	/**
	 * @see com.jdroid.android.activity.ActivityHelper#getNavDrawerItems()
	 */
	@Override
	public List<NavDrawerItem> getNavDrawerItems() {
		return Lists.<NavDrawerItem>newArrayList(AndroidNavDrawerItem.values());
	}

	@Override
	public NavDrawerHeaderBuilder createNavDrawerHeaderBuilder() {
		NavDrawerHeaderBuilder builder = super.createNavDrawerHeaderBuilder();
		builder.setBackground(R.drawable.hero);
		return builder;
	}
}
