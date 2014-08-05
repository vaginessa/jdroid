package com.jdroid.android.navdrawer;

import com.jdroid.android.ActionItem;

public interface NavDrawerItem extends ActionItem {
	
	public Boolean isMainAction();
	
	public Boolean isVisible();
}
