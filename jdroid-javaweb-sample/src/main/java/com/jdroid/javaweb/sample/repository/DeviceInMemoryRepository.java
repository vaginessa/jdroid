package com.jdroid.javaweb.sample.repository;

import com.jdroid.java.repository.InMemoryRepository;
import com.jdroid.javaweb.push.Device;
import com.jdroid.javaweb.push.DeviceRepository;
import com.jdroid.javaweb.push.DeviceType;

public class DeviceInMemoryRepository extends InMemoryRepository<Device> implements DeviceRepository {
	
	@Override
	public Device findByRegistrationToken(String registrationToken, DeviceType deviceType) {
		for(Device each : getAll()) {
			if (each.getRegistrationToken().equals(registrationToken) && each.getDeviceType().equals(deviceType)) {
				return each;
			}
		}
		return null;
	}
	
	@Override
	public Device findByInstanceId(String instanceId, DeviceType deviceType) {
		for(Device each : getAll()) {
			if (each.getInstanceId().equals(instanceId) && each.getDeviceType().equals(deviceType)) {
				return each;
			}
		}
		return null;
	}
}
