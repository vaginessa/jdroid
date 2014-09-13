package com.jdroid.sample.android.ui;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import com.jdroid.android.ActionItem;
import com.jdroid.android.ActivityLauncher;
import com.jdroid.sample.android.R;
import com.jdroid.sample.android.imageloader.ImageLoaderActivity;
import com.jdroid.sample.android.ui.ads.AdsActivity;
import com.jdroid.sample.android.ui.analytics.AnalyticsActivity;
import com.jdroid.sample.android.ui.exceptions.ExceptionHandlingActivity;
import com.jdroid.sample.android.ui.notifications.NotificationsActivity;
import com.jdroid.sample.android.ui.toasts.ToastsActivity;

public enum HomeItem implements ActionItem {
	
	ADMOB(R.string.adMob, R.drawable.default_item_selector, AdsActivity.class),
	ANDROID_UNVERSAL_IMAGE_LOADER(R.string.universalImageLoader, R.drawable.default_item_selector,
			ImageLoaderActivity.class),
	EXCEPTION_HANDLING(R.string.exceptionHandling, R.drawable.default_item_selector, ExceptionHandlingActivity.class),
	GOOGLE_ANALYTCS(R.string.googleAnalytics, R.drawable.default_item_selector, AnalyticsActivity.class),
	GCM(R.string.gcm, R.drawable.default_item_selector, ImageLoaderActivity.class),
	GOOGLE_MAPS(R.string.googleMaps, R.drawable.default_item_selector, ImageLoaderActivity.class),
	GOOGLE_PLUS(R.string.googlePlus, R.drawable.default_item_selector, ImageLoaderActivity.class),
	IN_APP_BILLING(R.string.inAppBilling, R.drawable.default_item_selector, ImageLoaderActivity.class),
	MERGE_ADAPTER(R.string.mergeAdapter, R.drawable.default_item_selector, ImageLoaderActivity.class),
	NOTIFICATIONS(R.string.notifications, R.drawable.default_item_selector, NotificationsActivity.class),
	TOASTS(R.string.toasts, R.drawable.default_item_selector, ToastsActivity.class);
	
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