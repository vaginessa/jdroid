package com.jdroid.javaweb.sample.repository;

import com.jdroid.java.firebase.FirebaseRepository;
import com.jdroid.java.firebase.auth.CustomTokenFirebaseAuthenticationStrategy;
import com.jdroid.javaweb.push.Device;
import com.jdroid.javaweb.push.DeviceRepository;
import com.jdroid.javaweb.push.DeviceType;
import com.jdroid.javaweb.sample.context.ServerApplication;

public class DeviceFirebaseRepository extends FirebaseRepository<Device> implements DeviceRepository {

	public DeviceFirebaseRepository() {
		super(new CustomTokenFirebaseAuthenticationStrategy() {
			@Override
			protected String getAuthToken() {
				return ServerApplication.get().getAppContext().getFirebaseAuthToken();
			}
		});
	}

	@Override
	protected String getFirebaseUrl() {
		return ServerApplication.get().getAppContext().getFirebaseUrl();
	}

	@Override
	protected String getPath() {
		return "devices";
	}

	@Override
	protected Class<Device> getEntityClass() {
		return Device.class;
	}

	@Override
	public Device findByRegistrationToken(String registrationToken, DeviceType deviceType) {
		for(Device each : findByField("registrationToken", registrationToken)) {
			if (each.getDeviceType().equals(deviceType)) {
				return each;
			}
		}
		return null;
	}

	@Override
	public Device findByInstanceId(String instanceId, DeviceType deviceType) {
		for(Device each : findByField("instanceId", instanceId)) {
			if (each.getDeviceType().equals(deviceType)) {
				return each;
			}
		}
		return null;
	}
}
