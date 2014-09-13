package com.jdroid.sample.android.ui.navdrawer;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import com.jdroid.android.ActivityLauncher;
import com.jdroid.android.navdrawer.NavDrawerItem;
import com.jdroid.sample.android.R;
import com.jdroid.sample.android.ui.HomeActivity;

public enum AndroidNavDrawerItem implements NavDrawerItem {
	
	WATCH_TO_WATCH(android.R.drawable.ic_menu_slideshow, R.string.home, HomeActivity.class, true),
	ABOUT(android.R.drawable.ic_menu_info_details, R.string.about, null, false) {
		
		@Override
		public void startActivity(FragmentActivity fragmentActivity) {
			// new AboutDialogFragment().show(fragmentActivity);
		}
	};
	;
	
	private Integer iconResource;
	private Integer nameResource;
	private Class<? extends FragmentActivity> activityClass;
	private Boolean mainAction;
	
	private AndroidNavDrawerItem(Integer iconResource, Integer nameResource,
			Class<? extends FragmentActivity> activityClass, Boolean mainAction) {
		this.iconResource = iconResource;
		this.nameResource = nameResource;
		this.activityClass = activityClass;
		this.mainAction = mainAction;
	}
	
	/**
	 * @see com.jdroid.android.ActionItem#startActivity(android.support.v4.app.FragmentActivity)
	 */
	@Override
	public void startActivity(FragmentActivity fragmentActivity) {
		ActivityLauncher.launchActivity(activityClass);
	}
	
	/**
	 * @see com.jdroid.android.ActionItem#matchesActivity(android.support.v4.app.FragmentActivity)
	 */
	@Override
	public Boolean matchesActivity(FragmentActivity fragmentActivity) {
		return fragmentActivity.getClass().equals(activityClass);
	}
	
	/**
	 * @see com.jdroid.android.ActionItem#getIconResource()
	 */
	@Override
	public Integer getIconResource() {
		return iconResource;
	}
	
	/**
	 * @see com.jdroid.android.ActionItem#getNameResource()
	 */
	@Override
	public Integer getNameResource() {
		return nameResource;
	}
	
	/**
	 * @see com.jdroid.android.ActionItem#getName()
	 */
	@Override
	public String getName() {
		return name();
	}
	
	/**
	 * @see com.jdroid.android.ActionItem#createFragment(java.lang.Object)
	 */
	@Override
	public Fragment createFragment(Object args) {
		return null;
	}
	
	/**
	 * @return the descriptionResource
	 */
	@Override
	public Integer getDescriptionResource() {
		return null;
	}
	
	/**
	 * @see com.jdroid.android.navdrawer.NavDrawerItem#isMainAction()
	 */
	@Override
	public Boolean isMainAction() {
		return mainAction;
	}
	
	/**
	 * @see com.jdroid.android.navdrawer.NavDrawerItem#isVisible()
	 */
	@Override
	public Boolean isVisible() {
		return true;
	}
	
}