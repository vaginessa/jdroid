package com.jdroid.android.google.gcm;

public class Device {

	private String deviceGroupId;
	private String registrationToken;

	public Device(String registrationToken, String deviceGroupId) {
		this.deviceGroupId = deviceGroupId;
		this.registrationToken = registrationToken;
	}

	public String getRegistrationToken() {
		return registrationToken;
	}

	public String getDeviceGroupId() {
		return deviceGroupId;
	}
}
