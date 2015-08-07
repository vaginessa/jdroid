package com.jdroid.android.about;

import android.app.Activity;
import com.jdroid.android.intent.IntentUtils;

public class Library {
	
	private String libraryKey;
	private Integer nameResId;
	private Integer descriptionResId;
	private String url;
	
	public Library(String libraryKey, Integer nameResId, Integer descriptionResId, String url) {
		this.libraryKey = libraryKey;
		this.nameResId = nameResId;
		this.descriptionResId = descriptionResId;
		this.url = url;
	}
	
	public Integer getNameResId() {
		return nameResId;
	}
	
	public Integer getDescriptionResId() {
		return descriptionResId;
	}
	
	public String getUrl() {
		return url;
	}
	
	public void onSelected(Activity activity) {
		IntentUtils.startUrl(activity, url);
	}
	
	public String getLibraryKey() {
		return libraryKey;
	}
}
