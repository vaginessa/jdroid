package com.jdroid.android.navdrawer;

import com.jdroid.android.tabs.TabAction;

/**
 * 
 * @author Maxi Rosson
 */
public interface NavDrawerItem extends TabAction {
	
	public Boolean isMainAction();
	
	public Boolean isVisible();
}
