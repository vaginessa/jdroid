package com.jdroid.javaweb.push;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import com.jdroid.java.exception.UnexpectedException;
import com.jdroid.javaweb.domain.Entity;

@javax.persistence.Entity
public class Device extends Entity {
	
	@ManyToOne
	@JoinColumn(name = "deviceGroupId", nullable = true)
	private DeviceGroup deviceGroup;
	
	private String deviceId;
	private String registrationId;
	
	@Enumerated(value = EnumType.STRING)
	private DeviceType deviceType;
	
	/**
	 * Default constructor.
	 */
	@SuppressWarnings("unused")
	private Device() {
		// Do nothing, is required by hibernate
	}
	
	public Device(String deviceId, String registrationId, DeviceType deviceType) {
		
		if (deviceType == null) {
			throw new UnexpectedException("The device type is required");
		}
		this.deviceType = deviceType;
		
		this.deviceId = deviceId;
		this.registrationId = registrationId;
	}
	
	public void updateRegistrationId(String registrationId) {
		this.registrationId = registrationId;
	}
	
	/**
	 * @return the deviceId
	 */
	public String getDeviceId() {
		return deviceId;
	}
	
	/**
	 * @return the registrationId
	 */
	public String getRegistrationId() {
		return registrationId;
	}
	
	/**
	 * @return the deviceType
	 */
	public DeviceType getDeviceType() {
		return deviceType;
	}
	
	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Device [deviceId=" + deviceId + ", registrationId=" + registrationId + ", deviceType=" + deviceType
				+ "]";
	}
	
	public void unassignDeviceGroup() {
		if (deviceGroup != null) {
			deviceGroup.removeDevice(this);
		}
	}
	
	/**
	 * @param deviceGroup the deviceGroup to set
	 */
	void setDeviceGroup(DeviceGroup deviceGroup) {
		this.deviceGroup = deviceGroup;
	}
	
	/**
	 * @return the deviceGroup
	 */
	public DeviceGroup getDeviceGroup() {
		return deviceGroup;
	}
	
}
