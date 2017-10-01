package com.jdroid.android.share;

import com.jdroid.android.utils.AndroidUtils;
import com.jdroid.android.utils.ExternalAppsUtils;

public class SmsSharingItem extends AppSharingItem {
	
	public SmsSharingItem(SharingData sharingData) {
		super(sharingData);
	}
	
	@Override
	public SharingMedium getSharingMedium() {
		return SharingMedium.SMS;
	}
	
	@Override
	public Boolean isEnabled() {
		return !AndroidUtils.isPreKitkat() && super.isEnabled()
				&& !getPackageName().equals(ExternalAppsUtils.HANGOUTS_PACKAGE_NAME);
	}
}
