package com.jdroid.javaweb.push;

import com.google.common.collect.Lists;
import com.jdroid.java.collections.Maps;

import java.util.List;
import java.util.Map;

public class PushResponse {

	private DeviceType deviceType;
	private List<String> registrationTokensToRemove;
	private Map<String, String> registrationTokensToReplace;
	private List<String> registrationTokensToRetry;

	public PushResponse(DeviceType deviceType) {
		this.deviceType = deviceType;
		registrationTokensToRemove = Lists.newArrayList();
		registrationTokensToReplace = Maps.newHashMap();
		registrationTokensToRetry = Lists.newArrayList();
	}
	
	public void addRegistrationTokenToRemove(String registrationTokenToRemove) {
		registrationTokensToRemove.add(registrationTokenToRemove);
	}
	
	public void addRegistrationTokenToReplace(String oldRegistrationToken, String newRegistrationToken) {
		registrationTokensToReplace.put(oldRegistrationToken, newRegistrationToken);
	}

	public void addRegistrationTokenToRetry(String registrationTokenToRetry) {
		registrationTokensToRetry.add(registrationTokenToRetry);
	}
	
	public List<String> getRegistrationTokensToRemove() {
		return registrationTokensToRemove;
	}
	public Map<String, String> getRegistrationTokensToReplace() {
		return registrationTokensToReplace;
	}

	public List<String> getRegistrationTokensToRetry() {
		return registrationTokensToRetry;
	}

	public DeviceType getDeviceType() {
		return deviceType;
	}
}
