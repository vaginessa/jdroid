package com.jdroid.android.share;

public class HangoutsSharingItem extends AppSharingItem {
	
	public HangoutsSharingItem(SharingData sharingData) {
		super(sharingData);
	}
	
	@Override
	public SharingMedium getSharingMedium() {
		return SharingMedium.HANGOUTS;
	}
	
	@Override
	public Integer getMinimumVersionCode() {
		return 23142355;
	}
}
