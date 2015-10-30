package com.jdroid.javaweb.push;

import java.util.Map;

public interface PushMessage {
	
	public Map<String, String> getParameters();
	
	public void addParameter(String key, String value);
	
	public void addParameter(String key, Boolean value);
	
	public void addParameter(String key, Integer value);
	
	public void addParameter(String key, Long value);

	public DeviceType getDeviceType();
	
}
