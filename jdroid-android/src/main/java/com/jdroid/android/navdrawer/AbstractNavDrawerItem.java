package com.jdroid.android.navdrawer;

import android.app.Activity;

import com.jdroid.android.activity.ActivityLauncher;

public class AbstractNavDrawerItem implements NavDrawerItem {

	private Integer itemId;
	private Class<? extends Activity> activityClass;

	public AbstractNavDrawerItem(Integer itemId) {
		this(itemId, null);
	}

	public AbstractNavDrawerItem(Integer itemId, Class<? extends Activity> activityClass) {
		this.itemId = itemId;
		this.activityClass = activityClass;
	}

	@Override
	public void startActivity() {
		ActivityLauncher.launchActivity(activityClass);
	}

	@Override
	public Boolean matchesActivity(Activity activity) {
		return activity.getClass().equals(activityClass);
	}

	@Override
	public Integer getItemId() {
		return itemId;
	}

	@Override
	public Class<? extends Activity> getActivityClass() {
		return activityClass;
	}
}
