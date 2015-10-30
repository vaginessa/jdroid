package com.jdroid.javaweb.google.gcm;

import com.jdroid.java.json.JsonMap;
import com.jdroid.java.marshaller.Marshaller;
import com.jdroid.java.marshaller.MarshallerMode;

import java.util.HashMap;
import java.util.Map;

public class GcmDataMarshaller implements Marshaller<HashMap<String, String>, JsonMap> {

	@Override
	public JsonMap marshall(HashMap<String, String> map, MarshallerMode mode, Map<String, String> extras) {
		JsonMap jsonMap = new JsonMap(mode, extras);
		for(Map.Entry<String, String> entry : map.entrySet()) {
			jsonMap.put(entry.getKey(),entry.getValue());
		}
		return jsonMap;
	}
}