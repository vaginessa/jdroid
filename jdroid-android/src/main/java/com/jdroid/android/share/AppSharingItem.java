package com.jdroid.android.share;

import android.support.annotation.NonNull;

public abstract class AppSharingItem extends SharingItem {
	
	private SharingData sharingData;
	
	public AppSharingItem(@NonNull SharingData sharingData) {
		this.sharingData = sharingData;
	}
	
	@Override
	public void share() {
		SharingDataItem sharingDataItem = sharingData.getShareInfoItemMap().get(getSharingMedium().getName());
		if (sharingDataItem == null) {
			sharingDataItem = sharingData.getDefaultSharingDataItem();
		}
		ShareUtils.share(getSharingMedium(), sharingData.getShareKey(), sharingDataItem.getText());
	}
	
	@NonNull
	public abstract SharingMedium getSharingMedium();
	
	@Override
	public String getPackageName() {
		return getSharingMedium().getPackageName();
	}
	
}
