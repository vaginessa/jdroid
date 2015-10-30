package com.jdroid.javaweb.push;

import com.jdroid.java.http.parser.json.JsonParser;
import com.jdroid.java.json.JSONObject;

public class DeviceParser extends JsonParser<JSONObject> {

	private String instanceId;
	private String userAgent;

	public DeviceParser(String instanceId, String userAgent) {
		this.instanceId = instanceId;
		this.userAgent = userAgent;
	}

	@Override
	public Object parse(JSONObject json) {
		return new Device(instanceId, DeviceType.find(userAgent),
				json.optString("registrationToken"), json.optString("deviceGroupId"));
	}
}