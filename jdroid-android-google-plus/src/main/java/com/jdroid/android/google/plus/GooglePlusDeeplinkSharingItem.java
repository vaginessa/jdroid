package com.jdroid.android.google.plus;

import android.support.v4.app.FragmentActivity;

import com.jdroid.android.AbstractApplication;
import com.jdroid.android.share.SharingItem;
import com.jdroid.android.utils.ExternalAppsUtils;

public abstract class GooglePlusDeeplinkSharingItem extends SharingItem {
	
	/**
	 * @see com.jdroid.android.share.SharingItem#getPackageName()
	 */
	@Override
	public String getPackageName() {
		return ExternalAppsUtils.GOOGLE_PLUS_PACKAGE_NAME;
	}
	
	/**
	 * @see com.jdroid.android.share.SharingItem#share()
	 */
	@Override
	public void share() {
		GooglePlusHelperFragment.get((FragmentActivity)AbstractApplication.get().getCurrentActivity()).shareDeeplink(
			getContent(), getLink());
	}
	
	protected abstract String getContent();
	
	protected abstract String getLink();
}
