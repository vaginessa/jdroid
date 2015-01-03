package com.jdroid.android.share;

import com.jdroid.android.utils.ExternalAppsUtils;

public abstract class TelegramSharingItem extends AppSharingItem {
	
	/**
	 * @see com.jdroid.android.share.SharingItem#share()
	 */
	@Override
	public void share() {
		ShareUtils.shareOnTelegram(getShareKey(), getShareText());
	}
	
	/**
	 * @see com.jdroid.android.share.SharingItem#getPackageName()
	 */
	@Override
	public String getPackageName() {
		return ExternalAppsUtils.TELEGRAM_PACKAGE_NAME;
	}
}
