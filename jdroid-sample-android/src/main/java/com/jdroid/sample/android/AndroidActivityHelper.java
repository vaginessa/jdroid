package com.jdroid.sample.android;

import java.util.List;
import android.app.Activity;
import com.jdroid.android.activity.ActivityHelper;
import com.jdroid.android.navdrawer.NavDrawerItem;
import com.jdroid.java.collections.Lists;
import com.jdroid.sample.android.ui.navdrawer.AndroidNavDrawerItem;

public class AndroidActivityHelper extends ActivityHelper {
	
	public AndroidActivityHelper(Activity activity) {
		super(activity);
	}
	
	/**
	 * @see com.jdroid.android.activity.ActivityHelper#isNavDrawerEnabled()
	 */
	@Override
	public Boolean isNavDrawerEnabled() {
		return true;
	}
	
	/**
	 * @see com.jdroid.android.activity.ActivityHelper#getNavDrawerItems()
	 */
	@Override
	public List<NavDrawerItem> getNavDrawerItems() {
		return Lists.<NavDrawerItem>newArrayList(AndroidNavDrawerItem.values());
	}
	
	/**
	 * @see com.jdroid.android.activity.ActivityHelper#isDarkTheme()
	 */
	@Override
	public Boolean isDarkTheme() {
		return false;
	}
	
}
