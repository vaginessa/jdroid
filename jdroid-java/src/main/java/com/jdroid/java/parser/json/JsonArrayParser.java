package com.jdroid.java.parser.json;

import com.jdroid.java.json.JSONArray;
import com.jdroid.java.json.JSONObject;

/**
 * 
 * @author Maxi Rosson
 */
public class JsonArrayParser extends JsonParser<JSONArray> {
	
	private JsonParser<JSONObject> parser;
	
	public JsonArrayParser(JsonParser<JSONObject> parser) {
		this.parser = parser;
	}
	
	/**
	 * @see com.jdroid.java.parser.json.JsonParser#parse(java.lang.Object)
	 */
	@Override
	public Object parse(JSONArray json) {
		return parseList(json, parser);
	}
}