package com.jdroid.android.share;

import com.jdroid.android.utils.ExternalAppsUtils;

public abstract class GooglePlusSharingItem extends AppSharingItem {
	
	@Override
	public void share() {
		ShareUtils.shareOnGooglePlus(getShareKey(), getShareText());
	}
	
	@Override
	public String getPackageName() {
		return ExternalAppsUtils.GOOGLE_PLUS_PACKAGE_NAME;
	}

	@Override
	public Integer getMinimumVersionCode() {
		return 416583705;
	}
}
