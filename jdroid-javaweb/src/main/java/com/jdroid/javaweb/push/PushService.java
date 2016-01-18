package com.jdroid.javaweb.push;

public interface PushService {
	
	public void addDevice(Device device, Boolean updateLastActiveTimestamp);

	public void removeDevice(String instanceId, DeviceType deviceType);
	
	public void send(PushMessage pushMessage);

	public void processResponse(PushResponse pushResponse);
}