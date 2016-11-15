package com.jdroid.android.firebase.dynamiclink;

import com.jdroid.java.http.parser.json.JsonParser;
import com.jdroid.java.json.JSONObject;

public class DynamicLinkParser extends JsonParser<JSONObject> {

	@Override
	public Object parse(JSONObject json) {
		return json.getString("shortLink");
	}
}
