package com.jdroid.android.social;

import com.jdroid.android.R;

public enum AccountType {
	
	GOOGLE_PLUS("googlePlus", "Google+", R.drawable.ic_google_plus),
	FACEBOOK("facebook", "Facebook", R.drawable.ic_facebook),
	TWITTER("twitter", "Twitter", R.drawable.ic_twitter),
	WHATSAPP("whatsapp", "WhatsApp", null),
	TELEGRAM("telegram", "Telegram", null),
	HANGOUTS("hangouts", "Hangouts", null),
	SMS("sms", "SMS", null),
	INTERNAL("internal", "Internal", null),
	ANONYMOUS("anonymous", "Anonymous", null);
	
	private String name;
	private String friendlyName;
	private Integer iconResId;
	
	AccountType(String name, String friendlyName, Integer iconResId) {
		this.name = name;
		this.friendlyName = friendlyName;
		this.iconResId = iconResId;
	}
	
	public String getName() {
		return name;
	}
	
	public String getFriendlyName() {
		return friendlyName;
	}
	
	public Integer getIconResId() {
		return iconResId;
	}
}
