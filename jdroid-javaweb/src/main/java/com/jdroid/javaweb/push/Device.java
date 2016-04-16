package com.jdroid.javaweb.push;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.jdroid.java.collections.Maps;
import com.jdroid.java.date.DateUtils;
import com.jdroid.java.domain.Entity;
import com.jdroid.java.exception.UnexpectedException;

import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Device extends Entity {
	
	private String instanceId;
	private DeviceType deviceType;

	private String deviceGroupId;
	private String registrationToken;

	private String deviceBrandName;
	private String deviceModelName;
	private String deviceOsVersion;
	private String appVersionCode;

	private String acceptLanguage;
	private Long creationTimestamp;
	private Long lastActiveTimestamp;

	private Map<String, Object> extras;

	private Map<String, Object> transientExtras;

	/**
	 * Default constructor.
	 */
	@SuppressWarnings("unused")
	private Device() {
		// Do nothing, is required by hibernate
	}
	
	public Device(String instanceId, DeviceType deviceType, String registrationToken, String deviceGroupId,
				  String deviceBrandName, String deviceModelName, String deviceOsVersion, String appVersionCode, String acceptLanguage) {
		
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

		this.acceptLanguage = acceptLanguage;
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

	public void setRegistrationToken(String registrationToken) {
		this.registrationToken = registrationToken;
	}

	public void setDeviceOsVersion(String deviceOsVersion) {
		this.deviceOsVersion = deviceOsVersion;
	}

	public void setAppVersionCode(String appVersionCode) {
		this.appVersionCode = appVersionCode;
	}

	public void setDeviceGroupId(String deviceGroupId) {
		this.deviceGroupId = deviceGroupId;
	}

	public String getAcceptLanguage() {
		return acceptLanguage;
	}

	public void setAcceptLanguage(String acceptLanguage) {
		this.acceptLanguage = acceptLanguage;
	}

	public Long getCreationTimestamp() {
		return creationTimestamp;
	}

	public void setCreationTimestamp(Long creationTimestamp) {
		this.creationTimestamp = creationTimestamp;
	}

	@JsonIgnore
	public Map<String, Object> getTransientExtras() {
		return transientExtras;
	}

	public void setTransientExtras(Map<String, Object> transientExtras) {
		this.transientExtras = transientExtras;
	}

	@Override
	public String toString() {
		final StringBuffer sb = new StringBuffer("Device{");
		sb.append("instanceId='").append(instanceId).append('\'');
		sb.append(", deviceType=").append(deviceType);
		sb.append(", deviceGroupId='").append(deviceGroupId).append('\'');
		sb.append(", registrationToken='").append(registrationToken).append('\'');
		sb.append(", deviceBrandName='").append(deviceBrandName).append('\'');
		sb.append(", deviceModelName='").append(deviceModelName).append('\'');
		sb.append(", deviceOsVersion='").append(deviceOsVersion).append('\'');
		sb.append(", appVersionCode='").append(appVersionCode).append('\'');
		sb.append(", acceptLanguage='").append(acceptLanguage).append('\'');
		sb.append(", creationTimestamp='").append(DateUtils.getDate(creationTimestamp)).append('\'');
		if (lastActiveTimestamp != null) {
			sb.append(", lastActiveTimestamp=").append(DateUtils.getDate(lastActiveTimestamp));
		}
		sb.append(", extras='").append(extras).append('\'');
		sb.append(", transientExtras='").append(transientExtras).append('\'');
		sb.append("} ");
		sb.append(super.toString());
		return sb.toString();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		if (!super.equals(o)) return false;

		Device device = (Device)o;

		if (instanceId != null ? !instanceId.equals(device.instanceId) : device.instanceId != null) return false;
		if (deviceType != device.deviceType) return false;
		if (deviceGroupId != null ? !deviceGroupId.equals(device.deviceGroupId) : device.deviceGroupId != null)
			return false;
		if (registrationToken != null ? !registrationToken.equals(device.registrationToken) : device.registrationToken != null)
			return false;
		if (deviceBrandName != null ? !deviceBrandName.equals(device.deviceBrandName) : device.deviceBrandName != null)
			return false;
		if (deviceModelName != null ? !deviceModelName.equals(device.deviceModelName) : device.deviceModelName != null)
			return false;
		if (deviceOsVersion != null ? !deviceOsVersion.equals(device.deviceOsVersion) : device.deviceOsVersion != null)
			return false;
		if (appVersionCode != null ? !appVersionCode.equals(device.appVersionCode) : device.appVersionCode != null)
			return false;
		if (acceptLanguage != null ? !acceptLanguage.equals(device.acceptLanguage) : device.acceptLanguage != null)
			return false;
		if (extras != null ? !extras.equals(device.extras) : device.extras != null)
			return false;
		return !(lastActiveTimestamp != null ? !lastActiveTimestamp.equals(device.lastActiveTimestamp) : device.lastActiveTimestamp != null);

	}

	public Map<String, Object> getExtras() {
		return extras;
	}

	public Object getExtra(String key) {
		return extras != null ? extras.get(key) : null;
	}

	public void addExtra(String key, Object value) {
		if (extras == null) {
			this.extras = Maps.newHashMap();
		}
		extras.put(key, value);
	}

	public void setExtras(Map<String, Object> extras) {
		this.extras = extras;
	}
}
