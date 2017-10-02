package com.jdroid.android.share;

import android.support.annotation.NonNull;

public abstract class AppSharingItem extends SharingItem {
	
	private SharingData sharingData;
	
	public AppSharingItem(@NonNull SharingData sharingData) {
		this.sharingData = sharingData;
	}
	
	@Override
	public void share() {
		ShareUtils.share(getSharingMedium(), sharingData.getShareKey(),
				sharingData.getShareInfoItemMap().get(getSharingMedium().getName()).getText());
	}
	
	@NonNull
	public abstract SharingMedium getSharingMedium();
	
	@Override
	public String getPackageName() {
		return getSharingMedium().getPackageName();
	}
	
	@Override
	public Boolean isEnabled() {
		return sharingData.getShareInfoItemMap().containsKey(getSharingMedium().getName()) && super.isEnabled();
	}
}
