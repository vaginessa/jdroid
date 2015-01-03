package com.jdroid.android.share;

import android.provider.Settings;
import com.jdroid.android.AbstractApplication;
import com.jdroid.android.utils.AndroidUtils;
import com.jdroid.android.utils.ExternalAppsUtils;

public abstract class SmsSharingItem extends AppSharingItem {
	
	/**
	 * @see com.jdroid.android.share.SharingItem#share()
	 */
	@Override
	public void share() {
		ShareUtils.shareOnSmsApp(getPackageName(), getShareKey(), getShareText());
	}
	
	/**
	 * @see com.jdroid.android.share.SharingItem#isEnabled()
	 */
	@Override
	public Boolean isEnabled() {
		return !AndroidUtils.isPreKitkat() && super.isEnabled()
				&& !getPackageName().equals(ExternalAppsUtils.HANGOUTS_PACKAGE_NAME);
	}
	
	/**
	 * @see com.jdroid.android.share.SharingItem#getPackageName()
	 */
	@Override
	public String getPackageName() {
		return Settings.Secure.getString(AbstractApplication.get().getContentResolver(), "sms_default_application");
	}
}
