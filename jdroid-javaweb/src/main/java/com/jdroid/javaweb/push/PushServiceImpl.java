package com.jdroid.javaweb.push;

import com.jdroid.java.concurrent.ExecutorUtils;
import com.jdroid.java.utils.LoggerUtils;
import com.jdroid.javaweb.context.Application;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map.Entry;

@Service
public class PushServiceImpl implements PushService {
	
	private static final Logger LOGGER = LoggerUtils.getLogger(PushServiceImpl.class);
	
	@Autowired
	private DeviceRepository deviceRepository;

	@Override
	public void addDevice(Device device) {
		Device deviceToUpdate = deviceRepository.findByInstanceId(device.getInstanceId(), device.getDeviceType());
		if (deviceToUpdate != null) {
			if (isDeviceUpdateRequired(deviceToUpdate, device)) {
				deviceToUpdate.setLastActiveTimestamp(System.currentTimeMillis());
				deviceToUpdate.setRegistrationToken(device.getRegistrationToken());
				deviceToUpdate.setDeviceGroupId(device.getDeviceGroupId());
				deviceToUpdate.setAppVersionCode(device.getAppVersionCode());
				deviceToUpdate.setDeviceOsVersion(device.getDeviceOsVersion());
				deviceRepository.update(deviceToUpdate);
			}
		} else {
			device.setLastActiveTimestamp(System.currentTimeMillis());
			deviceRepository.add(device);
		}
	}

	protected Boolean isDeviceUpdateRequired(Device oldDevice, Device newDevice) {
		newDevice.setLastActiveTimestamp(oldDevice.getLastActiveTimestamp());
		newDevice.setId(oldDevice.getId());
		return !oldDevice.equals(newDevice) || System.currentTimeMillis() - oldDevice.getLastActiveTimestamp() > Application.get().getAppContext().getDeviceUpdateRequiredDuration();
	}

	@Override
	public void removeDevice(String instanceId, DeviceType deviceType) {
		Device deviceToRemove = deviceRepository.findByInstanceId(instanceId, deviceType);
		if (deviceToRemove != null) {
			deviceRepository.remove(deviceToRemove);
		}
	}

	@Override
	public void send(final PushMessage pushMessage) {
		ExecutorUtils.execute(new PushProcessor(this, pushMessage));
	}

	@Override
	public void processResponse(PushResponse pushResponse) {
		for (String each : pushResponse.getRegistrationTokensToRemove()) {
			Device deviceToRemove = deviceRepository.findByRegistrationToken(each, pushResponse.getDeviceType());
			if (deviceToRemove != null) {
				deviceRepository.remove(deviceToRemove);
			}
		}

		for (Entry<String, String> entry : pushResponse.getRegistrationTokensToReplace().entrySet()) {
			Device deviceToUpdate = deviceRepository.findByRegistrationToken(entry.getKey(), pushResponse.getDeviceType());
			if (deviceToUpdate != null) {
				deviceToUpdate.setRegistrationToken(entry.getValue());
				deviceRepository.update(deviceToUpdate);
			}
		}
	}

	private class PushProcessor implements Runnable {
		
		private PushService pushService;
		private PushMessage pushMessage;
		
		public PushProcessor(PushService pushService, PushMessage pushMessage) {
			this.pushService = pushService;
			this.pushMessage = pushMessage;
		}
		
		@Override
		public void run() {
			PushResponse pushResponse = pushMessage.getDeviceType().send(pushMessage);
			pushService.processResponse(pushResponse);
		}
	}
}
