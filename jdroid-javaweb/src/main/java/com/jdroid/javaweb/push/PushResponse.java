package com.jdroid.javaweb.push;

import com.google.common.collect.Lists;
import com.jdroid.java.collections.Maps;

import java.util.List;
import java.util.Map;

public class PushResponse {

	private DeviceType deviceType;
	private List<String> registrationTokensToRemove;
	private Map<String, String> registrationTokensToReplace;
	
	public PushResponse(DeviceType deviceType) {
		this.deviceType = deviceType;
		registrationTokensToRemove = Lists.newArrayList();
		registrationTokensToReplace = Maps.newHashMap();
	}
	
	public void addRegistrationTokenToRemove(String registrationTokenToRemove) {
		registrationTokensToRemove.add(registrationTokenToRemove);
	}
	
	public void addRegistrationTokenToReplace(String oldRegistrationToken, String newRegistrationToken) {
		registrationTokensToReplace.put(oldRegistrationToken, newRegistrationToken);
	}
	
	public List<String> getRegistrationTokensToRemove() {
		return registrationTokensToRemove;
	}
	
	public Map<String, String> getRegistrationTokensToReplace() {
		return registrationTokensToReplace;
	}

	public DeviceType getDeviceType() {
		return deviceType;
	}
}
