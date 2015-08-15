package com.jdroid.android.sample.ui.navdrawer;

import android.support.v4.app.FragmentActivity;

import com.jdroid.android.about.AboutActivity;
import com.jdroid.android.activity.ActivityLauncher;
import com.jdroid.android.navdrawer.NavDrawerItem;
import com.jdroid.android.sample.R;
import com.jdroid.android.sample.ui.home.HomeActivity;

public enum AndroidNavDrawerItem implements NavDrawerItem {
	
	HOME(R.id.home, HomeActivity.class),
	ABOUT(R.id.about, AboutActivity.class);
	
	private Integer itemId;
	private Class<? extends FragmentActivity> activityClass;

	AndroidNavDrawerItem(Integer itemId,
			Class<? extends FragmentActivity> activityClass) {
		this.itemId = itemId;
		this.activityClass = activityClass;
	}
	
	@Override
	public void startActivity() {
		ActivityLauncher.launchActivity(activityClass);
	}
	
	@Override
	public Boolean matchesActivity(FragmentActivity fragmentActivity) {
		return fragmentActivity.getClass().equals(activityClass);
	}
	
	@Override
	public Integer getItemId() {
		return itemId;
	}

	@Override
	public Class<? extends FragmentActivity> getActivityClass() {
		return activityClass;
	}
}