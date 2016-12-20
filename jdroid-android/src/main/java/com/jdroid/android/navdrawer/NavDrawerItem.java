package com.jdroid.android.navdrawer;

import android.app.Activity;

public interface NavDrawerItem {

	public Integer getItemId();

	public void startActivity();

	public Boolean matchesActivity(Activity activity);

	public Class<? extends Activity> getActivityClass();

}
