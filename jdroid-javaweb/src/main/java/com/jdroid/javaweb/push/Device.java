package com.jdroid.javaweb.push;

import com.jdroid.java.domain.Entity;
import com.jdroid.java.exception.UnexpectedException;

public class Device extends Entity {
	
	private String instanceId;
	private DeviceType deviceType;

	private String deviceGroupId;
	private String registrationToken;

	private String deviceBrandName;
	private String deviceModelName;
	private String deviceOsVersion;
	private String appVersionCode;

	private Long lastActiveTimestamp;

	/**
	 * Default constructor.
	 */
	@SuppressWarnings("unused")
	private Device() {
		// Do nothing, is required by hibernate
	}
	
	public Device(String instanceId, DeviceType deviceType, String registrationToken, String deviceGroupId,
				  String deviceBrandName, String deviceModelName, String deviceOsVersion, String appVersionCode) {
		
		if (instanceId == null) {
			throw new UnexpectedException("The instanceId is required");
		}

		if (deviceType == null) {
			throw new UnexpectedException("The device type is required");
		}

		this.instanceId = instanceId;
		this.deviceType = deviceType;

		this.deviceGroupId = deviceGroupId;
		this.registrationToken = registrationToken;

		this.deviceBrandName = deviceBrandName;
		this.deviceModelName = deviceModelName;
		this.deviceOsVersion = deviceOsVersion;
		this.appVersionCode = appVersionCode;
	}

	public void updateRegistrationToken(String registrationToken) {
		this.registrationToken = registrationToken;
	}

	public void updateDeviceGroupId(String deviceGroupId) {
		this.deviceGroupId = deviceGroupId;
	}

	public String getInstanceId() {
		return instanceId;
	}
	
	/**
	 * @return the registrationToken
	 */
	public String getRegistrationToken() {
		return registrationToken;
	}
	
	/**
	 * @return the deviceType
	 */
	public DeviceType getDeviceType() {
		return deviceType;
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

	public Long getLastActiveTimestamp() {
		return lastActiveTimestamp;
	}

	public void setLastActiveTimestamp(Long lastActiveTimestamp) {
		this.lastActiveTimestamp = lastActiveTimestamp;
	}

	@Override
	public String toString() {
		return "Device{" +
				"instanceId='" + instanceId + '\'' +
				", deviceType=" + deviceType +
				", deviceGroupId='" + deviceGroupId + '\'' +
				", registrationToken='" + registrationToken + '\'' +
				", deviceBrandName='" + deviceBrandName + '\'' +
				", deviceModelName='" + deviceModelName + '\'' +
				", deviceOsVersion='" + deviceOsVersion + '\'' +
				", appVersionCode='" + appVersionCode + '\'' +
				", lastActiveTimestamp=" + lastActiveTimestamp +
				"} " + super.toString();
	}
}
