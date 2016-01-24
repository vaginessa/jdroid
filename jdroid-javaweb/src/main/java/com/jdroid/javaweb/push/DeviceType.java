package com.jdroid.javaweb.push;

import com.jdroid.javaweb.google.gcm.GcmSender;

public enum DeviceType {
	ANDROID("android", GcmSender.get()),
	IOS("iOS", null);

	private String userAgent;
	private PushMessageSender pushMessageSender;
	
	DeviceType(String userAgent, PushMessageSender pushMessageSender) {
		this.pushMessageSender = pushMessageSender;
		this.userAgent = userAgent;
	}
	
	public static DeviceType find(String userAgent) {
		for (DeviceType each : values()) {
			if (each.userAgent.equalsIgnoreCase(userAgent)) {
				return each;
			}
		}
		return null;
	}

	public String getUserAgent() {
		return userAgent;
	}
	
	public PushResponse send(PushMessage pushMessage) {
		return pushMessageSender.send(pushMessage);
	}
}
