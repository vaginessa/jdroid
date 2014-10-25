package com.jdroid.android.social;

import com.jdroid.android.R;

public enum AccountType {
	
	GOOGLE_PLUS("googlePlus", "Google+", R.drawable.common_signin_btn_icon_normal_light),
	FACEBOOK("facebook", "Facebook", R.drawable.ic_facebook),
	TWITTER("twitter", "Twitter", null),
	INTERNAL("internal", "Internal", null),
	ANONYMOUS("anonymous", "Anonymous", null);
	
	private String name;
	private String friendlyName;
	private Integer iconResId;
	
	private AccountType(String name, String friendlyName, Integer iconResId) {
		this.name = name;
		this.friendlyName = friendlyName;
		this.iconResId = iconResId;
	}
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * @return the friendlyName
	 */
	public String getFriendlyName() {
		return friendlyName;
	}
	
	/**
	 * @return the iconResId
	 */
	public Integer getIconResId() {
		return iconResId;
	}
}
