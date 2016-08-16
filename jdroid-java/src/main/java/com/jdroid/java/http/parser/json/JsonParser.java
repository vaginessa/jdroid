package com.jdroid.java.http.parser.json;

import com.jdroid.java.collections.Lists;
import com.jdroid.java.json.JSONArray;
import com.jdroid.java.json.JSONException;
import com.jdroid.java.json.JSONObject;
import com.jdroid.java.utils.FileUtils;
import com.jdroid.java.utils.LoggerUtils;
import com.jdroid.java.utils.StringUtils;

import org.slf4j.Logger;

import java.io.InputStream;
import java.util.List;

/**
 * JSON input streams parser
 * 
 * @param <T>
 */
public abstract class JsonParser<T> implements com.jdroid.java.http.parser.Parser {
	
	private static final Logger LOGGER = LoggerUtils.getLogger(JsonParser.class);
	private static final String ARRAY_PREFIX = "[";
	
	@SuppressWarnings("unchecked")
	@Override
	public Object parse(String input) {
		
		LOGGER.trace("Parsing started.");
		try {
			LOGGER.trace("Response: " + input);
			
			// Create a wrapped JsonObject or JsonArray
			T json;
			if (input.startsWith(ARRAY_PREFIX)) {
				json = (T)new JSONArray(input);
			} else {
				json = (T)new JSONObject(input);
			}
			
			// Parse the JSONObject
			return parse(json);
		} catch (JSONException e) {
			String message = e.getMessage();
			if (message != null && input != null && message.startsWith("A JSONObject text must begin with '{'")) {
				throw new JSONException("Invalid json [" + input.substring(0, Math.min(input.length(), 50)) + "]");
			} else {
				throw e;
			}
		} finally {
			LOGGER.trace("Parsing finished.");
		}
	}
	
	@Override
	public Object parse(InputStream inputStream) {
		String content = FileUtils.toString(inputStream);
		return StringUtils.isNotBlank(content) ? parse(content) : null;
	}
	
	/**
	 * @param json
	 * @return The parsed object
	 */
	public abstract Object parse(T json);
	
	/**
	 * Parses a list of items.
	 * 
	 * @param <ITEM> The item's type.
	 * 
	 * @param jsonObject The {@link JSONObject} to parse.
	 * @param jsonKey The key for the Json array.
	 * @param parser The {@link JsonParser} to parse each list item.
	 * @return The parsed list.
	 */
	protected <ITEM> List<ITEM> parseList(JSONObject jsonObject, String jsonKey, JsonParser<JSONObject> parser) {
		return parseList(jsonObject.getJSONArray(jsonKey), parser);
	}
	
	/**
	 * Parses a list of items.
	 * 
	 * @param <ITEM> The item's type.
	 * 
	 * @param jsonArray The {@link JSONArray} to parse.
	 * @param parser The {@link JsonParser} to parse each list item.
	 * @return The parsed list.
	 */
	@SuppressWarnings("unchecked")
	protected <ITEM> List<ITEM> parseList(JSONArray jsonArray, JsonParser<JSONObject> parser) {
		List<ITEM> list = Lists.newArrayList();
		if (jsonArray != null) {
			int length = jsonArray.length();
			for (int i = 0; i < length; i++) {
				ITEM parse = (ITEM)parser.parse(jsonArray.getJSONObject(i));
				if (parse != null) {
					list.add(parse);
				}
			}
		}
		return list;
	}
}
