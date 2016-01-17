package com.jdroid.javaweb.push;

import com.jdroid.java.http.parser.json.JsonParser;
import com.jdroid.java.json.JSONObject;

public class DeviceParser extends JsonParser<JSONObject> {

	private String instanceId;
	private String userAgent;
	private String acceptLanguage;

	public DeviceParser(String instanceId, String userAgent, String acceptLanguage) {
		this.instanceId = instanceId;
		this.userAgent = userAgent;
		this.acceptLanguage = acceptLanguage;
	}

	@Override
	public Object parse(JSONObject json) {

		DeviceType deviceType = DeviceType.find(userAgent);
		String registrationToken = json.optString("registrationToken");
		String deviceGroupId = json.optString("deviceGroupId");
		String deviceBrandName = json.optString("deviceBrandName");
		String deviceModelName = json.optString("deviceModelName");
		String deviceOsVersion = json.optString("deviceOsVersion");
		String appVersionCode = json.optString("appVersionCode");

		return new Device(instanceId, deviceType, registrationToken, deviceGroupId,
				deviceBrandName, deviceModelName, deviceOsVersion, appVersionCode, acceptLanguage);
	}
}