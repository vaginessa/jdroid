package com.jdroid.javaweb.push;

import com.jdroid.java.repository.CacheWrapperRepository;
import com.jdroid.java.utils.LoggerUtils;

import org.slf4j.Logger;

public class DeviceCacheWrapperRepository extends CacheWrapperRepository<Device> implements DeviceRepository {

	private static final Logger LOGGER = LoggerUtils.getLogger(DeviceCacheWrapperRepository.class);

	public DeviceCacheWrapperRepository(DeviceRepository wrappedRepository) {
		super(wrappedRepository);
	}

	@Override
	public Device findByRegistrationToken(String registrationToken, DeviceType deviceType) {
		for (Device each : getCache().values()) {
			if (each.getRegistrationToken() != null && each.getRegistrationToken().equals(registrationToken) && each.getDeviceType().equals(deviceType)) {
				LOGGER.info("Retrieved object from cache: " + each.getClass().getSimpleName() + ". [ " + each + " ]");
				return each;
			}
		}

		Device device = ((DeviceRepository)getWrappedRepository()).findByRegistrationToken(registrationToken, deviceType);
		if (device != null) {
			getCache().put(device.getId(), device);
		}
		return device;
	}

	@Override
	public Device findByInstanceId(String instanceId, DeviceType deviceType) {
		for (Device each : getCache().values()) {
			if (each.getInstanceId() != null && each.getInstanceId().equals(instanceId) && each.getDeviceType().equals(deviceType)) {
				LOGGER.info("Retrieved object from cache: " + each.getClass().getSimpleName() + ". [ " + each + " ]");
				return each;
			}
		}

		Device device = ((DeviceRepository)getWrappedRepository()).findByInstanceId(instanceId, deviceType);
		if (device != null) {
			getCache().put(device.getId(), device);
		}
		return device;
	}
}