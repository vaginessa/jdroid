package com.jdroid.android.share;

import java.util.Map;

public class SharingData {
	
	private String shareKey;
	private Map<String, SharingDataItem> shareInfoItemMap;
	
	public SharingData(String shareKey, Map<String, SharingDataItem> shareInfoItemMap) {
		this.shareKey = shareKey;
		this.shareInfoItemMap = shareInfoItemMap;
	}
	
	public Map<String, SharingDataItem> getShareInfoItemMap() {
		return shareInfoItemMap;
	}
	
	public void setShareInfoItemMap(Map<String, SharingDataItem> shareInfoItemMap) {
		this.shareInfoItemMap = shareInfoItemMap;
	}
	
	public String getShareKey() {
		return shareKey;
	}
	
	public void setShareKey(String shareKey) {
		this.shareKey = shareKey;
	}
}
