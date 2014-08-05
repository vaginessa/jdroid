package com.jdroid.javaweb.push;

import com.jdroid.java.repository.Repository;

public interface DeviceRepository extends Repository<Device> {
	
	public Device find(String deviceId, DeviceType deviceType);
	
	public Device find(String deviceId, DeviceType deviceType, String registrationId);
	
}