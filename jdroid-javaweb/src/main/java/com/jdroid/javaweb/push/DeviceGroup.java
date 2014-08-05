package com.jdroid.javaweb.push;

import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.OneToMany;
import com.google.common.collect.Lists;
import com.jdroid.javaweb.domain.Entity;

@javax.persistence.Entity
public class DeviceGroup extends Entity {
	
	@OneToMany(mappedBy = "deviceGroup", cascade = CascadeType.ALL)
	private List<Device> devices = Lists.newArrayList();
	
	public void addDevice(Device device) {
		if (!devices.contains(device)) {
			device.setDeviceGroup(this);
			devices.add(device);
		}
	}
	
	public void removeDevice(Device device) {
		device.setDeviceGroup(null);
		devices.remove(device);
	}
	
	public Device find(String deviceId, DeviceType deviceType) {
		for (Device each : devices) {
			if (each.getDeviceId().equals(deviceId) && each.getDeviceType().equals(deviceType)) {
				return each;
			}
		}
		return null;
	}
	
	/**
	 * @return the devices
	 */
	public List<Device> getDevices() {
		return devices;
	}
}
