package com.jdroid.javaweb.push;

import com.jdroid.java.exception.UnexpectedException;
import com.jdroid.javaweb.domain.Entity;

public class Device extends Entity {
	
	private String instanceId;
	private DeviceType deviceType;

	private String deviceGroupId;
	private String registrationToken;

	/**
	 * Default constructor.
	 */
	@SuppressWarnings("unused")
	private Device() {
		// Do nothing, is required by hibernate
	}
	
	public Device(String instanceId, DeviceType deviceType, String registrationToken, String deviceGroupId) {
		
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

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Device [instanceId=" + instanceId + ", registrationToken=" + registrationToken + ", deviceType=" + deviceType
				+ "]";
	}
}
