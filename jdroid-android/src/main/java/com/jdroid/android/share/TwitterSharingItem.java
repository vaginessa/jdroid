package com.jdroid.android.share;

import com.jdroid.android.utils.ExternalAppsUtils;

public abstract class TwitterSharingItem extends AppSharingItem {
	
	@Override
	public void share() {
		ShareUtils.shareOnTwitter(getShareKey(), getShareText());
	}
	
	@Override
	public String getPackageName() {
		return ExternalAppsUtils.TWITTER_PACKAGE_NAME;
	}
	
	@Override
	public Integer getMinimumVersionCode() {
		return 420;
	}
}
