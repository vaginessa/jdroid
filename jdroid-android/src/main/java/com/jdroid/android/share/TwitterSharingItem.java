package com.jdroid.android.share;

public class TwitterSharingItem extends AppSharingItem {
	
	public TwitterSharingItem(SharingData sharingData) {
		super(sharingData);
	}
	
	@Override
	public SharingMedium getSharingMedium() {
		return SharingMedium.TWITTER;
	}
	
	@Override
	public Integer getMinimumVersionCode() {
		return 420;
	}
}
