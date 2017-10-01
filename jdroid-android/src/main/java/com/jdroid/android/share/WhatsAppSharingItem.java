package com.jdroid.android.share;

public class WhatsAppSharingItem extends AppSharingItem {
	
	public WhatsAppSharingItem(SharingData sharingData) {
		super(sharingData);
	}
	
	@Override
	public SharingMedium getSharingMedium() {
		return SharingMedium.WHATSAPP;
	}
	
	@Override
	public Integer getMinimumVersionCode() {
		return 49000;
	}
}
