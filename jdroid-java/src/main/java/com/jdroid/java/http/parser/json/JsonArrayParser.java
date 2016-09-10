package com.jdroid.java.http.parser.json;

import com.jdroid.java.json.JSONArray;
import com.jdroid.java.json.JSONObject;

public class JsonArrayParser extends JsonParser<JSONArray> {
	
	private JsonParser<JSONObject> parser;
	
	public JsonArrayParser(JsonParser<JSONObject> parser) {
		this.parser = parser;
	}
	
	@Override
	public Object parse(JSONArray json) {
		return parseList(json, parser);
	}
}