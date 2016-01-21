package com.jdroid.javaweb.google.gcm;

import com.jdroid.java.json.JsonMap;
import com.jdroid.java.marshaller.Marshaller;
import com.jdroid.java.marshaller.MarshallerMode;

import java.util.Map;

public class GcmMessageMarshaller implements Marshaller<GcmMessage, JsonMap> {

	public static final String COLLAPSE_KEY = "collapse_key";
	public static final String REGISTRATION_IDS = "registration_ids";
	public static final String TO = "to";
	public static final String PRIORITY = "priority";
	public static final String DELAY_WHILE_IDLE = "delay_while_idle";
	public static final String TIME_TO_LIVE = "time_to_live";
	public static final String DATA = "data";

	@Override
	public JsonMap marshall(GcmMessage gcmMessage, MarshallerMode mode, Map<String, String> extras) {
		JsonMap jsonMap = new JsonMap(mode, extras);
		jsonMap.put(TO, gcmMessage.getTo());
		jsonMap.put(REGISTRATION_IDS, gcmMessage.getRegistrationIds());
		jsonMap.put(COLLAPSE_KEY, gcmMessage.getCollapseKey());
		jsonMap.put(PRIORITY, gcmMessage.getPriority().getParameter());
		jsonMap.put(DELAY_WHILE_IDLE, gcmMessage.isDelayWhileIdle());
		jsonMap.put(TIME_TO_LIVE, gcmMessage.getTimeToLive());
		jsonMap.put(DATA, gcmMessage.getParameters());
		return jsonMap;
	}
}