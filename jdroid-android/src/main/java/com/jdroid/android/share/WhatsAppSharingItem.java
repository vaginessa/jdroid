package com.jdroid.android.share;

import com.jdroid.android.utils.ExternalAppsUtils;

public abstract class WhatsAppSharingItem extends AppSharingItem {
	
	@Override
	public void share() {
		ShareUtils.shareOnWhatsApp(getShareKey(), getShareText());
	}
	
	@Override
	public String getPackageName() {
		return ExternalAppsUtils.WHATSAPP_PACKAGE_NAME;
	}
	
	@Override
	public Integer getMinimumVersionCode() {
		return 49000;
	}
}
