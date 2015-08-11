package com.jdroid.javaweb.push;

import com.jdroid.javaweb.push.gcm.GcmSender;

import java.util.List;

public enum DeviceType {
	ANDROID("android", GcmSender.get()),
	IPHONE("iphone", null),
	BLACKBERRY("blackberry", null),
	WINDOWS_PHONE("windowsPhone", null);
	
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
	
	public PushResponse send(List<Device> devices, PushMessage pushMessage) {
		return pushMessageSender.send(devices, pushMessage);
	}
}
