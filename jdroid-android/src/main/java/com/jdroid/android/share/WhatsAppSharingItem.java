package com.jdroid.android.share;

import com.jdroid.android.utils.ExternalAppsUtils;

public abstract class WhatsAppSharingItem extends AppSharingItem {
	
	/**
	 * @see com.jdroid.android.share.SharingItem#share()
	 */
	@Override
	public void share() {
		ShareUtils.shareOnWhatsApp(getShareKey(), getShareText());
	}
	
	/**
	 * @see com.jdroid.android.share.SharingItem#getPackageName()
	 */
	@Override
	public String getPackageName() {
		return ExternalAppsUtils.WHATSAPP_PACKAGE_NAME;
	}
}
