package com.jdroid.javaweb.sample.api.controller;

import com.jdroid.java.json.JsonMap;
import com.jdroid.java.marshaller.Marshaller;
import com.jdroid.java.marshaller.MarshallerMode;

import java.util.Map;

public class SampleResponseMarshaller implements Marshaller<SampleResponse, JsonMap> {

	@Override
	public JsonMap marshall(SampleResponse sampleResponse, MarshallerMode mode, Map<String, String> extras) {
		JsonMap jsonMap = new JsonMap(mode, extras);
		jsonMap.put("sampleKey", sampleResponse.getSampleKey());
		return jsonMap;
	}
}
