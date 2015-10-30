package com.jdroid.javaweb.google.gcm;

import com.jdroid.java.http.parser.json.JsonParser;
import com.jdroid.java.json.JSONObject;

import java.util.List;

public class GcmResponseParser extends JsonParser<JSONObject> {

	@Override
	public Object parse(JSONObject json) {
		GcmResponse gcmResponse = new GcmResponse();
		gcmResponse.setSuccess(json.optInt("success"));
		gcmResponse.setFailure(json.optInt("failure"));
		gcmResponse.setCanonicalIds(json.optInt("canonical_ids"));
		gcmResponse.setMulticastId(json.optInt("multicast_id"));
		List<GcmResult> results = parseList(json, "results", new GcmResultParser());
		gcmResponse.setResults(results);
		return gcmResponse;
	}

	private class GcmResultParser extends JsonParser<JSONObject> {

		@Override
		public Object parse(JSONObject json) {
			GcmResult gcmResult = new GcmResult();
			gcmResult.setMessageId(json.optString("message_id"));
			gcmResult.setRegistrationId(json.optString("registration_id"));
			gcmResult.setError(json.optString("error"));
			return gcmResult;
		}
	}
}