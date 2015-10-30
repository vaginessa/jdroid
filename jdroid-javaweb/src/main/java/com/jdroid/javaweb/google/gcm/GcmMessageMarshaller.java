package com.jdroid.javaweb.google.gcm;

import com.jdroid.java.json.JsonMap;
import com.jdroid.java.marshaller.Marshaller;
import com.jdroid.java.marshaller.MarshallerMode;

import java.util.Map;

public class GcmMessageMarshaller implements Marshaller<GcmMessage, JsonMap> {

	@Override
	public JsonMap marshall(GcmMessage gcmMessage, MarshallerMode mode, Map<String, String> extras) {
		JsonMap jsonMap = new JsonMap(mode, extras);
		jsonMap.put("to", gcmMessage.getTo());
		jsonMap.put("registration_ids", gcmMessage.getRegistrationIds());
		jsonMap.put("collapse_key", gcmMessage.getCollapseKey());
		jsonMap.put("priority", gcmMessage.getPriority().getParameter());
		jsonMap.put("delay_while_idle", gcmMessage.isDelayWhileIdle());
		jsonMap.put("time_to_live", gcmMessage.getTimeToLive());
		if (!gcmMessage.getParameters().isEmpty()) {
			jsonMap.put("data", gcmMessage.getParameters());
		}
		return jsonMap;
	}
}