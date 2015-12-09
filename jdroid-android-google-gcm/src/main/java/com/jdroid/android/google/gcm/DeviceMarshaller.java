package com.jdroid.android.google.gcm;

import com.jdroid.java.json.JsonMap;
import com.jdroid.java.marshaller.Marshaller;
import com.jdroid.java.marshaller.MarshallerMode;

import java.util.Map;

public class DeviceMarshaller implements Marshaller<Device, JsonMap> {

	@Override
	public JsonMap marshall(Device device, MarshallerMode mode, Map<String, String> extras) {
		JsonMap jsonMap = new JsonMap(mode, extras);
		jsonMap.put("deviceGroupId", device.getDeviceGroupId());
		jsonMap.put("registrationToken", device.getRegistrationToken());

		jsonMap.put("deviceBrandName", device.getDeviceBrandName());
		jsonMap.put("deviceModelName", device.getDeviceModelName());
		jsonMap.put("deviceOsVersion", device.getDeviceOsVersion());
		jsonMap.put("appVersionCode", device.getAppVersionCode());
		return jsonMap;
	}
}
