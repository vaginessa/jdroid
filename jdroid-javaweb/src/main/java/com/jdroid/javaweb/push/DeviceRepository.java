package com.jdroid.javaweb.push;

import com.jdroid.java.repository.Repository;

public interface DeviceRepository extends Repository<Device> {
	
	public Device findByRegistrationToken(String registrationToken, DeviceType deviceType);
	
	public Device findByInstanceId(String instanceId, DeviceType deviceType);
	
}