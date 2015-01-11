package com.jdroid.android.share;

import com.jdroid.android.utils.ExternalAppsUtils;

public abstract class GooglePlusSharingItem extends AppSharingItem {
	
	/**
	 * @see com.jdroid.android.share.SharingItem#share()
	 */
	@Override
	public void share() {
		ShareUtils.shareOnGooglePlus(getShareKey(), getShareText());
	}
	
	/**
	 * @see com.jdroid.android.share.SharingItem#getPackageName()
	 */
	@Override
	public String getPackageName() {
		return ExternalAppsUtils.GOOGLE_PLUS_PACKAGE_NAME;
	}
}
