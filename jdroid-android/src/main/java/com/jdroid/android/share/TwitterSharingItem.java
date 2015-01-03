package com.jdroid.android.share;

import com.jdroid.android.utils.ExternalAppsUtils;

public abstract class TwitterSharingItem extends AppSharingItem {
	
	/**
	 * @see com.jdroid.android.share.SharingItem#share()
	 */
	@Override
	public void share() {
		ShareUtils.shareOnTwitter(getShareKey(), getShareText());
	}
	
	/**
	 * @see com.jdroid.android.share.SharingItem#getPackageName()
	 */
	@Override
	public String getPackageName() {
		return ExternalAppsUtils.TWITTER_PACKAGE_NAME;
	}
}
