package com.jdroid.javaweb.push;

import java.util.List;

/**
 * 
 * @author Maxi Rosson
 */
public interface PushService {
	
	public void enableDevice(Long deviceGroupId, String deviceId, DeviceType deviceType, String registrationId);
	
	public void assignDevice(Long deviceGroupId, String deviceId, DeviceType deviceType);
	
	public void unassignDevice(Long deviceGroupId, String deviceId, DeviceType deviceType);
	
	public void send(PushMessage pushMessage, Device... devices);
	
	public void send(PushMessage pushMessage, List<Device> devices);
	
	public void removeDevice(String deviceId, DeviceType deviceType, String registrationId);
	
}