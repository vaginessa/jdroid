package com.jdroid.android.share;

public abstract class AppSharingItem extends SharingItem {
	
	private SharingData sharingData;
	
	public AppSharingItem(SharingData sharingData) {
		this.sharingData = sharingData;
	}
	
	public SharingData getSharingData() {
		return sharingData;
	}
	
	@Override
	public void share() {
		ShareUtils.share(getSharingMedium(), getSharingData().getShareKey(),
				getSharingData().getShareInfoItemMap().get(getSharingMedium().getName()).getText());
	}
	
	public abstract SharingMedium getSharingMedium();
	
	@Override
	public String getPackageName() {
		return getSharingMedium().getPackageName();
	}
}
