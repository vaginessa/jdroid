package com.jdroid.android.share;

import com.jdroid.android.utils.ExternalAppsUtils;

public abstract class HangoutsSharingItem extends AppSharingItem {
	
	/**
	 * @see com.jdroid.android.share.SharingItem#share()
	 */
	@Override
	public void share() {
		ShareUtils.shareOnHangouts(getShareKey(), getShareText());
	}
	
	/**
	 * @see com.jdroid.android.share.SharingItem#getPackageName()
	 */
	@Override
	public String getPackageName() {
		return ExternalAppsUtils.HANGOUTS_PACKAGE_NAME;
	}
}
