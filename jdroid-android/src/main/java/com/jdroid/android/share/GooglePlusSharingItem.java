package com.jdroid.android.share;

public class GooglePlusSharingItem extends AppSharingItem {
	
	public GooglePlusSharingItem(SharingData sharingData) {
		super(sharingData);
	}
	
	@Override
	public SharingMedium getSharingMedium() {
		return SharingMedium.GOOGLE_PLUS;
	}

	@Override
	public Integer getMinimumVersionCode() {
		return 416583705;
	}
}
