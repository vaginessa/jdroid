package com.jdroid.sample.android.ui;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import com.jdroid.android.ActionItem;
import com.jdroid.android.ActivityLauncher;
import com.jdroid.sample.android.R;
import com.jdroid.sample.android.imageloader.ImageLoaderActivity;
import com.jdroid.sample.android.ui.ads.AdsActivity;

public enum HomeItem implements ActionItem {
	
	ADS(R.string.ads, R.drawable.default_item_selector, AdsActivity.class),
	IMAGE_LOADER(R.string.imageLoader, R.drawable.default_item_selector, ImageLoaderActivity.class);
	
	private Integer resourceId;
	private Integer iconId;
	private Class<? extends FragmentActivity> activityClass;
	
	private HomeItem(Integer resourceId, Integer iconId, Class<? extends FragmentActivity> activityClass) {
		this.resourceId = resourceId;
		this.iconId = iconId;
		this.activityClass = activityClass;
	}
	
	/**
	 * @see com.jdroid.android.ActionItem#getNameResource()
	 */
	@Override
	public Integer getNameResource() {
		return resourceId;
	}
	
	/**
	 * @see com.jdroid.android.ActionItem#getIconResource()
	 */
	@Override
	public Integer getIconResource() {
		return iconId;
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
		return activityClass.equals(fragmentActivity.getClass());
	}
	
	/**
	 * @see com.jdroid.android.ActionItem#createFragment(java.lang.Object)
	 */
	@Override
	public Fragment createFragment(Object args) {
		return null;
	}
	
	/**
	 * @see com.jdroid.android.ActionItem#getName()
	 */
	@Override
	public String getName() {
		return name();
	}
	
	/**
	 * @see com.jdroid.android.ActionItem#getDescriptionResource()
	 */
	@Override
	public Integer getDescriptionResource() {
		return null;
	}
}