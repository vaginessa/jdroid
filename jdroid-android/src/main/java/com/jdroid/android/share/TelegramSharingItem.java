package com.jdroid.android.share;

public class TelegramSharingItem extends AppSharingItem {
	
	public TelegramSharingItem(SharingData sharingData) {
		super(sharingData);
	}
	
	@Override
	public SharingMedium getSharingMedium() {
		return SharingMedium.TELEGRAM;
	}
}
