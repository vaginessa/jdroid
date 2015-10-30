package com.jdroid.javaweb.push;

import com.jdroid.java.json.JsonMap;
import com.jdroid.java.marshaller.Marshaller;
import com.jdroid.java.marshaller.MarshallerMode;

import java.util.Map;

public class DeviceMarshaller implements Marshaller<Device, JsonMap> {

	@Override
	public JsonMap marshall(Device device, MarshallerMode mode, Map<String, String> extras) {
		JsonMap jsonMap = new JsonMap(mode, extras);
		jsonMap.put("instanceId", device.getInstanceId());
		jsonMap.put("deviceGroupId", device.getDeviceGroupId());
		jsonMap.put("deviceType", device.getDeviceType());
		jsonMap.put("registrationToken", device.getRegistrationToken());
		return jsonMap;
	}
}
