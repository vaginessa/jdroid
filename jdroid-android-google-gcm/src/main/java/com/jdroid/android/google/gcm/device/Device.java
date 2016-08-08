package com.jdroid.android.google.gcm.device;

import com.jdroid.android.utils.AndroidUtils;
import com.jdroid.android.utils.AppUtils;
import com.jdroid.android.utils.DeviceUtils;

public class Device {

	private String deviceGroupId;
	private String registrationToken;

	private String deviceBrandName;
	private String deviceModelName;
	private String deviceOsVersion;
	private String appVersionCode;

	public Device(String registrationToken, String deviceGroupId) {
		this.deviceGroupId = deviceGroupId;
		this.registrationToken = registrationToken;
		deviceBrandName = DeviceUtils.getDeviceManufacturer();
		deviceModelName = DeviceUtils.getDeviceModel();
		deviceOsVersion = AndroidUtils.getPlatformVersion();
		appVersionCode = AppUtils.getVersionCode().toString();
	}

	public String getRegistrationToken() {
		return registrationToken;
	}

	public String getDeviceGroupId() {
		return deviceGroupId;
	}

	public String getDeviceBrandName() {
		return deviceBrandName;
	}

	public String getDeviceModelName() {
		return deviceModelName;
	}

	public String getDeviceOsVersion() {
		return deviceOsVersion;
	}

	public String getAppVersionCode() {
		return appVersionCode;
	}
}
