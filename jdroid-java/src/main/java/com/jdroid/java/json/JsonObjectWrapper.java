package com.jdroid.java.json;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import com.jdroid.java.collections.Lists;
import com.jdroid.java.utils.DateUtils;

/**
 * {@link JSONObject} Wrapper
 * 
 * @author Maxi Rosson
 */
public class JsonObjectWrapper {
	
	private JSONObject jsonObject;
	
	public JsonObjectWrapper() {
		this(new JSONObject());
	}
	
	public JsonObjectWrapper(String string) {
		this(new JSONObject(string));
	}
	
	public JsonObjectWrapper(JSONObject jsonObject) {
		this.jsonObject = jsonObject;
	}
	
	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object o) {
		return jsonObject.equals(o);
	}
	
	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return jsonObject.hashCode();
	}
	
	/**
	 * @param key
	 * @param value
	 * @return the {@link JsonObjectWrapper}
	 * @see com.jdroid.java.json.JSONObject#accumulate(java.lang.String, java.lang.Object)
	 */
	public JsonObjectWrapper accumulate(String key, Object value) {
		return new JsonObjectWrapper(jsonObject.accumulate(key, value));
	}
	
	/**
	 * @param key
	 * @return the {@link Object}
	 * @see com.jdroid.java.json.JSONObject#get(java.lang.String)
	 */
	public Object get(String key) {
		return jsonObject.get(key);
	}
	
	/**
	 * @param key
	 * @return the boolean
	 * @see com.jdroid.java.json.JSONObject#getBoolean(java.lang.String)
	 */
	public boolean getBoolean(String key) {
		return jsonObject.getBoolean(key);
	}
	
	/**
	 * @param key
	 * @return the double
	 */
	public double getDouble(String key) {
		return jsonObject.getDouble(key);
	}
	
	public float getFloat(String key) {
		return (float)jsonObject.getDouble(key);
	}
	
	/**
	 * @param key
	 * @return the int
	 * @see com.jdroid.java.json.JSONObject#getInt(java.lang.String)
	 */
	public int getInt(String key) {
		return jsonObject.getInt(key);
	}
	
	/**
	 * @param key
	 * @return the {@link JsonArrayWrapper}
	 * @see com.jdroid.java.json.JSONObject#getJSONArray(java.lang.String)
	 */
	public JsonArrayWrapper getJSONArray(String key) {
		return new JsonArrayWrapper(jsonObject.getJSONArray(key));
	}
	
	/**
	 * @param key
	 * @return the {@link JsonObjectWrapper}
	 * @see com.jdroid.java.json.JSONObject#getJSONObject(java.lang.String)
	 */
	public JsonObjectWrapper getJSONObject(String key) {
		return new JsonObjectWrapper(jsonObject.getJSONObject(key));
	}
	
	/**
	 * @param key
	 * @return the long
	 * @see com.jdroid.java.json.JSONObject#getLong(java.lang.String)
	 */
	public long getLong(String key) {
		return jsonObject.getLong(key);
	}
	
	/**
	 * @param key
	 * @return the {@link String}
	 * @see com.jdroid.java.json.JSONObject#getString(java.lang.String)
	 */
	public String getString(String key) {
		String value = null;
		if (!jsonObject.isNull(key)) {
			value = jsonObject.getString(key);
		}
		return value;
	}
	
	public Date getDate(String key) {
		return getDate(key, DateUtils.YYYYMMDDHHMMSSZ_DATE_FORMAT);
	}
	
	public Date getDate(String key, String dateFormat) {
		Date date = null;
		if (!jsonObject.isNull(key)) {
			String value = jsonObject.getString(key);
			date = DateUtils.parse(value, dateFormat);
		}
		return date;
	}
	
	public Date optDate(String key) {
		return optDate(key, DateUtils.YYYYMMDDHHMMSSZ_DATE_FORMAT);
	}
	
	public Date optDate(String key, String dateFormat) {
		return optDate(key, dateFormat, null);
	}
	
	public Date optDate(String key, String dateFormat, Date defaultDate) {
		if (hasNotNull(key)) {
			String value = jsonObject.getString(key);
			return DateUtils.parse(value, dateFormat);
		}
		return defaultDate;
	}
	
	/**
	 * @param key
	 * @return the boolean
	 * @see com.jdroid.java.json.JSONObject#has(java.lang.String)
	 */
	public boolean has(String key) {
		return jsonObject.has(key);
	}
	
	public boolean hasNotNull(String key) {
		return jsonObject.has(key) && !jsonObject.isNull(key);
	}
	
	/**
	 * @param key
	 * @return the boolean
	 * @see com.jdroid.java.json.JSONObject#isNull(java.lang.String)
	 */
	public boolean isNull(String key) {
		return jsonObject.isNull(key);
	}
	
	/**
	 * @return the {@link Iterator}
	 * @see com.jdroid.java.json.JSONObject#keys()
	 */
	public Iterator<?> keys() {
		return jsonObject.keys();
	}
	
	/**
	 * @return the int
	 * @see com.jdroid.java.json.JSONObject#length()
	 */
	public int length() {
		return jsonObject.length();
	}
	
	/**
	 * @return the {@link JsonArrayWrapper}
	 * @see com.jdroid.java.json.JSONObject#names()
	 */
	public JsonArrayWrapper names() {
		return new JsonArrayWrapper(jsonObject.names());
	}
	
	/**
	 * @param key
	 * @return the {@link Object}
	 * @see com.jdroid.java.json.JSONObject#opt(java.lang.String)
	 */
	public Object opt(String key) {
		if (hasNotNull(key)) {
			return jsonObject.get(key);
		}
		return null;
	}
	
	/**
	 * @param key
	 * @return the boolean
	 * @see com.jdroid.java.json.JSONObject#optBoolean(java.lang.String)
	 */
	public Boolean optBoolean(String key) {
		return optBoolean(key, null);
	}
	
	/**
	 * @param key
	 * @param defaultValue
	 * @return the boolean
	 * @see com.jdroid.java.json.JSONObject#optBoolean(java.lang.String, boolean)
	 */
	public Boolean optBoolean(String key, Boolean defaultValue) {
		if (hasNotNull(key)) {
			return jsonObject.getBoolean(key);
		}
		return defaultValue;
	}
	
	/**
	 * @param key
	 * @return the double
	 * @see com.jdroid.java.json.JSONObject#optDouble(java.lang.String)
	 */
	public Double optDouble(String key) {
		return optDouble(key, null);
	}
	
	/**
	 * @param key
	 * @param defaultValue
	 * @return the double
	 * @see com.jdroid.java.json.JSONObject#optDouble(java.lang.String, double)
	 */
	public Double optDouble(String key, Double defaultValue) {
		if (hasNotNull(key)) {
			return jsonObject.getDouble(key);
		}
		return defaultValue;
	}
	
	/**
	 * @param key
	 * @return the float
	 * @see com.jdroid.java.json.JSONObject#optDouble(java.lang.String)
	 */
	public Float optFloat(String key) {
		return optFloat(key, null);
	}
	
	/**
	 * @param key
	 * @param defaultValue
	 * @return the float
	 * @see com.jdroid.java.json.JSONObject#optDouble(java.lang.String, double)
	 */
	public Float optFloat(String key, Float defaultValue) {
		if (hasNotNull(key)) {
			return (float)jsonObject.getDouble(key);
		}
		return defaultValue;
	}
	
	/**
	 * @param key
	 * @return the int
	 * @see com.jdroid.java.json.JSONObject#optInt(java.lang.String)
	 */
	public Integer optInt(String key) {
		return optInt(key, null);
	}
	
	/**
	 * @param key
	 * @param defaultValue
	 * @return the int
	 * @see com.jdroid.java.json.JSONObject#optInt(java.lang.String, int)
	 */
	public Integer optInt(String key, Integer defaultValue) {
		if (hasNotNull(key)) {
			return jsonObject.getInt(key);
		}
		return defaultValue;
	}
	
	/**
	 * @param key
	 * @return the {@link JsonArrayWrapper}
	 * @see com.jdroid.java.json.JSONObject#optJSONArray(java.lang.String)
	 */
	public JsonArrayWrapper optJSONArray(String key) {
		JSONArray optJsonArray = jsonObject.optJSONArray(key);
		return optJsonArray != null ? new JsonArrayWrapper(optJsonArray) : null;
	}
	
	public List<String> optList(String key) {
		JsonArrayWrapper jsonArray = optJSONArray(key);
		List<String> list = null;
		if (jsonArray != null) {
			int length = jsonArray.length();
			list = Lists.newArrayList();
			for (int i = 0; i < length; i++) {
				list.add(jsonArray.getString(i));
			}
		}
		return list;
	}
	
	/**
	 * @param key
	 * @return the {@link JsonObjectWrapper}
	 * @see com.jdroid.java.json.JSONObject#optJSONObject(java.lang.String)
	 */
	public JsonObjectWrapper optJSONObject(String key) {
		JSONObject optJsonObject = jsonObject.optJSONObject(key);
		return optJsonObject != null ? new JsonObjectWrapper(optJsonObject) : null;
	}
	
	/**
	 * @param key
	 * @return the long
	 * @see com.jdroid.java.json.JSONObject#optLong(java.lang.String)
	 */
	public Long optLong(String key) {
		return optLong(key, null);
	}
	
	/**
	 * @param key
	 * @param defaultValue
	 * @return the long
	 * @see com.jdroid.java.json.JSONObject#optLong(java.lang.String, long)
	 */
	public Long optLong(String key, Long defaultValue) {
		if (hasNotNull(key)) {
			return jsonObject.getLong(key);
		}
		return defaultValue;
	}
	
	/**
	 * @param key
	 * @return the {@link String}
	 * @see com.jdroid.java.json.JSONObject#optString(java.lang.String)
	 */
	public String optString(String key) {
		return optString(key, null);
	}
	
	/**
	 * @param key
	 * @param defaultValue
	 * @return the {@link String}
	 * @see com.jdroid.java.json.JSONObject#optString(java.lang.String, java.lang.String)
	 */
	public String optString(String key, String defaultValue) {
		if (hasNotNull(key)) {
			return jsonObject.getString(key);
		}
		return defaultValue;
	}
	
	/**
	 * @param key
	 * @param value
	 * @return the {@link JsonObjectWrapper}
	 * @see com.jdroid.java.json.JSONObject#put(java.lang.String, boolean)
	 */
	public JsonObjectWrapper put(String key, boolean value) {
		jsonObject.put(key, value);
		return this;
	}
	
	/**
	 * @param key
	 * @param value
	 * @return the {@link JsonObjectWrapper}
	 * @see com.jdroid.java.json.JSONObject#put(java.lang.String, double)
	 */
	public JsonObjectWrapper put(String key, double value) {
		jsonObject.put(key, value);
		return this;
	}
	
	/**
	 * @param key
	 * @param value
	 * @return the {@link JsonObjectWrapper}
	 * @see com.jdroid.java.json.JSONObject#put(java.lang.String, int)
	 */
	public JsonObjectWrapper put(String key, int value) {
		jsonObject.put(key, value);
		return this;
	}
	
	/**
	 * @param key
	 * @param value
	 * @return the {@link JsonObjectWrapper}
	 * @see com.jdroid.java.json.JSONObject#put(java.lang.String, long)
	 */
	public JsonObjectWrapper put(String key, long value) {
		jsonObject.put(key, value);
		return this;
	}
	
	/**
	 * @param key
	 * @param value
	 * @return the {@link JsonObjectWrapper}
	 * @see com.jdroid.java.json.JSONObject#put(java.lang.String, java.lang.Object)
	 */
	public JsonObjectWrapper put(String key, Object value) {
		jsonObject.put(key, value);
		return this;
	}
	
	/**
	 * @param key
	 * @param value
	 * @return the {@link JsonObjectWrapper}
	 * @see com.jdroid.java.json.JSONObject#putOpt(java.lang.String, java.lang.Object)
	 */
	public JsonObjectWrapper putOpt(String key, Object value) {
		jsonObject.put(key, value);
		return this;
	}
	
	/**
	 * @param key
	 * @return the {@link Object}
	 * @see com.jdroid.java.json.JSONObject#remove(java.lang.String)
	 */
	public Object remove(String key) {
		return jsonObject.remove(key);
	}
	
	/**
	 * @param names
	 * @return the {@link JsonArrayWrapper}
	 * @see com.jdroid.java.json.JSONObject#toJSONArray(com.jdroid.java.json.JSONArray)
	 */
	public JsonArrayWrapper toJSONArray(JSONArray names) {
		return new JsonArrayWrapper(jsonObject.toJSONArray(names));
	}
	
	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return jsonObject.toString();
	}
	
	/**
	 * @param indentFactor
	 * @return the {@link String}
	 * @see com.jdroid.java.json.JSONObject#toString(int)
	 */
	public String toString(int indentFactor) {
		return jsonObject.toString(indentFactor);
	}
	
	/**
	 * @return the jsonObject
	 */
	public JSONObject getJsonObject() {
		return jsonObject;
	}
	
}
