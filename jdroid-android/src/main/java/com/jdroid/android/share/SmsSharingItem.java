package com.jdroid.android.share;

import android.provider.Settings;
import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.utils.AndroidUtils;
import com.jdroid.android.utils.ExternalAppsUtils;

public abstract class SmsSharingItem extends AppSharingItem {
	
	@Override
	public void share() {
		ShareUtils.shareOnSmsApp(getPackageName(), getShareKey(), getShareText());
	}
	
	@Override
	public Boolean isEnabled() {
		return !AndroidUtils.isPreKitkat() && super.isEnabled()
				&& !getPackageName().equals(ExternalAppsUtils.HANGOUTS_PACKAGE_NAME);
	}
	
	@Override
	public String getPackageName() {
		return Settings.Secure.getString(AbstractApplication.get().getContentResolver(), "sms_default_application");
	}
}
