package com.jdroid.android.share;

import com.jdroid.android.utils.ExternalAppsUtils;

public abstract class HangoutsSharingItem extends AppSharingItem {
	
	@Override
	public void share() {
		ShareUtils.shareOnHangouts(getShareKey(), getShareText());
	}
	
	@Override
	public String getPackageName() {
		return ExternalAppsUtils.HANGOUTS_PACKAGE_NAME;
	}
	
	@Override
	public Integer getMinimumVersionCode() {
		return 22181736;
	}
}
