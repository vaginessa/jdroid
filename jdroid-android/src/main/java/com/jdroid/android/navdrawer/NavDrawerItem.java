package com.jdroid.android.navdrawer;

import android.support.v4.app.FragmentActivity;

public interface NavDrawerItem {

	public Integer getItemId();

	public void startActivity();

	public Boolean matchesActivity(FragmentActivity fragmentActivity);

	public Class<? extends FragmentActivity> getActivityClass();

}
