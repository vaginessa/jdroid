package com.jdroid.javaweb.google.gcm;

import com.jdroid.java.http.parser.json.JsonParser;
import com.jdroid.java.json.JSONObject;

public class GcmMessageParser extends JsonParser<JSONObject> {

	@Override
	public Object parse(JSONObject json) {
		GcmMessage gcmMessage = new GcmMessage();
		gcmMessage.setTo(json.optString(GcmMessageMarshaller.TO));
		gcmMessage.setRegistrationIds(json.optList(GcmMessageMarshaller.REGISTRATION_IDS));
		gcmMessage.setCollapseKey(json.optString(GcmMessageMarshaller.COLLAPSE_KEY));
		GcmMessagePriority priority = GcmMessagePriority.findByParameter(json.optString(GcmMessageMarshaller.PRIORITY));
		if (priority != null) {
			gcmMessage.setPriority(priority);
		}
		Boolean delayWhileIdle = json.optBoolean(GcmMessageMarshaller.DELAY_WHILE_IDLE);
		if (delayWhileIdle != null) {
			gcmMessage.setDelayWhileIdle(delayWhileIdle);
		}
		gcmMessage.setTimeToLive(json.optInt(GcmMessageMarshaller.TIME_TO_LIVE));
		JSONObject data = json.getJSONObject(GcmMessageMarshaller.DATA);
		for (Object each : data.keySet()) {
			gcmMessage.addParameter(each.toString(), data.getString(each.toString()));
		}
		return gcmMessage;
	}
}
