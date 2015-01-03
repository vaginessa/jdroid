package com.jdroid.android.about;

import android.app.Activity;
import com.jdroid.android.utils.IntentUtils;

public class Library {
	
	private Integer nameResId;
	private Integer descriptionResId;
	private String url;
	
	public Library(Integer nameResId, Integer descriptionResId, String url) {
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
	
}
