package com.jdroid.javaweb.push;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jdroid.java.utils.ExecutorUtils;
import com.jdroid.java.utils.LoggerUtils;

/**
 * 
 * @author Maxi Rosson
 */
@Service
public class PushServiceImpl implements PushService {
	
	private static final Logger LOGGER = LoggerUtils.getLogger(PushServiceImpl.class);
	
	@Autowired
	private DeviceGroupRepository deviceGroupRepository;
	
	@Autowired
	private DeviceRepository deviceRepository;
	
	/**
	 * @see com.jdroid.javaweb.push.PushService#enableDevice(java.lang.Long, java.lang.String,
	 *      com.jdroid.javaweb.push.DeviceType, java.lang.String)
	 */
	@Transactional
	@Override
	public void enableDevice(Long deviceGroupId, String deviceId, DeviceType deviceType, String registrationId) {
		
		if (deviceGroupId == null) {
			Device device = deviceRepository.find(deviceId, deviceType);
			if (device != null) {
				device.updateRegistrationId(registrationId);
				LOGGER.info("Updated anonymous " + device);
				
				// If the user removed the app data or reinstalled the app while it was logged, we unassign the device
				device.unassignDeviceGroup();
			} else {
				device = new Device(deviceId, registrationId, deviceType);
				deviceRepository.add(device);
				LOGGER.info("Created anonymous " + device);
			}
		} else {
			
			DeviceGroup deviceGroup = deviceGroupRepository.get(deviceGroupId);
			Device device = deviceGroup.find(deviceId, deviceType);
			if (device != null) {
				device.updateRegistrationId(registrationId);
				LOGGER.info("Updated " + device);
			} else {
				device = deviceRepository.find(deviceId, deviceType);
				if (device == null) {
					device = new Device(deviceId, registrationId, deviceType);
					LOGGER.info("Created " + device);
				} else {
					device.updateRegistrationId(registrationId);
					LOGGER.info("Updated " + device);
					device.unassignDeviceGroup();
				}
				deviceGroup.addDevice(device);
			}
		}
	}
	
	/**
	 * @see com.jdroid.javaweb.push.PushService#assignDevice(java.lang.Long, java.lang.String,
	 *      com.jdroid.javaweb.push.DeviceType)
	 */
	@Override
	@Transactional
	public void assignDevice(Long deviceGroupId, String deviceId, DeviceType deviceType) {
		DeviceGroup deviceGroup = deviceGroupRepository.get(deviceGroupId);
		Device device = deviceGroup.find(deviceId, deviceType);
		if (device == null) {
			device = deviceRepository.find(deviceId, deviceType);
			if (device != null) {
				device.unassignDeviceGroup();
				deviceGroup.addDevice(device);
				LOGGER.info("Assigned " + device);
			}
		}
	}
	
	/**
	 * @see com.jdroid.javaweb.push.PushService#unassignDevice(java.lang.Long, java.lang.String,
	 *      com.jdroid.javaweb.push.DeviceType)
	 */
	@Transactional
	@Override
	public void unassignDevice(Long deviceGroupId, String deviceId, DeviceType deviceType) {
		DeviceGroup deviceGroup = deviceGroupRepository.get(deviceGroupId);
		Device device = deviceGroup.find(deviceId, deviceType);
		if (device != null) {
			deviceGroup.removeDevice(device);
			LOGGER.info("Unassigned " + device);
		}
	}
	
	/**
	 * @see com.jdroid.javaweb.push.PushService#removeDevice(java.lang.String, com.jdroid.javaweb.push.DeviceType,
	 *      java.lang.String)
	 */
	@Override
	@Transactional
	public void removeDevice(String deviceId, DeviceType deviceType, String registrationId) {
		Device device = deviceRepository.find(deviceId, deviceType, registrationId);
		if (device != null) {
			device.unassignDeviceGroup();
			deviceRepository.remove(device);
		}
	}
	
	/**
	 * @see com.jdroid.javaweb.push.PushService#send(com.jdroid.javaweb.push.PushMessage,
	 *      com.jdroid.javaweb.push.Device[])
	 */
	@Override
	public void send(PushMessage pushMessage, Device... devices) {
		send(pushMessage, Lists.newArrayList(devices));
	}
	
	/**
	 * @see com.jdroid.javaweb.push.PushService#send(com.jdroid.javaweb.push.PushMessage, java.util.List)
	 */
	@Override
	public void send(final PushMessage pushMessage, List<Device> devices) {
		Map<DeviceType, List<Device>> devicesMap = Maps.newHashMap();
		for (Device device : devices) {
			if (device.getRegistrationId() != null) {
				List<Device> devicesByType = devicesMap.get(device.getDeviceType());
				if (devicesByType == null) {
					devicesByType = Lists.newArrayList();
					devicesMap.put(device.getDeviceType(), devicesByType);
				}
				devicesByType.add(device);
			}
		}
		for (final Entry<DeviceType, List<Device>> entry : devicesMap.entrySet()) {
			ExecutorUtils.execute(new PushProcessor(this, entry.getKey(), entry.getValue(), pushMessage));
		}
	}
	
	private class PushProcessor implements Runnable {
		
		private PushService pushService;
		private DeviceType deviceType;
		private List<Device> devices;
		private PushMessage pushMessage;
		
		public PushProcessor(PushService pushService, DeviceType deviceType, List<Device> devices,
				PushMessage pushMessage) {
			this.pushService = pushService;
			this.deviceType = deviceType;
			this.devices = devices;
			this.pushMessage = pushMessage;
		}
		
		/**
		 * @see java.lang.Runnable#run()
		 */
		@Override
		public void run() {
			PushResponse pushResponse = deviceType.send(devices, pushMessage);
			for (Device device : pushResponse.getDevicesToRemove()) {
				pushService.removeDevice(device.getDeviceId(), device.getDeviceType(), device.getRegistrationId());
			}
			for (Device device : pushResponse.getDevicesToUpdate()) {
				pushService.enableDevice(device.getDeviceGroup() != null ? device.getDeviceGroup().getId() : null,
					device.getDeviceId(), device.getDeviceType(), device.getRegistrationId());
			}
		}
	}
}
