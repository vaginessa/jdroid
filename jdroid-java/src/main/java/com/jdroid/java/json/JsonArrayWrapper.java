package com.jdroid.java.json;

import java.util.Collection;

/**
 * {@link JSONArray} Wrapper
 * 
 * @author Maxi Rosson
 */
public class JsonArrayWrapper {
	
	private JSONArray jsonArray;
	
	/**
	 * @param jsonArray
	 */
	public JsonArrayWrapper(JSONArray jsonArray) {
		this.jsonArray = jsonArray;
	}
	
	public JsonArrayWrapper(String string) {
		this(new JSONArray(string));
	}
	
	public JsonArrayWrapper(Collection<Object> values) {
		this(new JSONArray(values));
	}
	
	public JsonArrayWrapper() {
		this(new JSONArray());
	}
	
	/**
	 * @param object
	 * @return the boolean
	 * @see com.jdroid.java.json.JSONArray#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object object) {
		return jsonArray.equals(object);
	}
	
	/**
	 * @param index
	 * @return the {@link Object}
	 * @see com.jdroid.java.json.JSONArray#get(int)
	 */
	public Object get(int index) {
		return jsonArray.get(index);
	}
	
	/**
	 * @param index
	 * @return the boolean
	 * @see com.jdroid.java.json.JSONArray#getBoolean(int)
	 */
	public boolean getBoolean(int index) {
		return jsonArray.getBoolean(index);
	}
	
	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return jsonArray.hashCode();
	}
	
	/**
	 * @param index
	 * @return the double
	 * @see com.jdroid.java.json.JSONArray#getDouble(int)
	 */
	public double getDouble(int index) {
		return jsonArray.getDouble(index);
	}
	
	/**
	 * @param index
	 * @return the int
	 * @see com.jdroid.java.json.JSONArray#getInt(int)
	 */
	public int getInt(int index) {
		return jsonArray.getInt(index);
	}
	
	/**
	 * @param index
	 * @return the {@link JsonArrayWrapper}
	 * @see com.jdroid.java.json.JSONArray#getJSONArray(int)
	 */
	public JsonArrayWrapper getJSONArray(int index) {
		return new JsonArrayWrapper(jsonArray.getJSONArray(index));
	}
	
	/**
	 * @param index
	 * @return the {@link JsonObjectWrapper}
	 * @see com.jdroid.java.json.JSONArray#getJSONObject(int)
	 */
	public JsonObjectWrapper getJSONObject(int index) {
		return new JsonObjectWrapper(jsonArray.getJSONObject(index));
	}
	
	/**
	 * @param index
	 * @return the long
	 * @see com.jdroid.java.json.JSONArray#getLong(int)
	 */
	public long getLong(int index) {
		return jsonArray.getLong(index);
	}
	
	/**
	 * @param index
	 * @return the {@link String}
	 * @see com.jdroid.java.json.JSONArray#getString(int)
	 */
	public String getString(int index) {
		return jsonArray.getString(index);
	}
	
	/**
	 * @param index
	 * @return the boolean
	 * @see com.jdroid.java.json.JSONArray#isNull(int)
	 */
	public boolean isNull(int index) {
		return jsonArray.isNull(index);
	}
	
	/**
	 * @param separator
	 * @return the String
	 * @see com.jdroid.java.json.JSONArray#join(java.lang.String)
	 */
	public String join(String separator) {
		return jsonArray.join(separator);
	}
	
	/**
	 * @return the int
	 * @see com.jdroid.java.json.JSONArray#length()
	 */
	public int length() {
		return jsonArray.length();
	}
	
	/**
	 * @param index
	 * @return the {@link Object}
	 * @see com.jdroid.java.json.JSONArray#opt(int)
	 */
	public Object opt(int index) {
		return jsonArray.opt(index);
	}
	
	/**
	 * @param index
	 * @return the boolean
	 * @see com.jdroid.java.json.JSONArray#optBoolean(int)
	 */
	public boolean optBoolean(int index) {
		return jsonArray.optBoolean(index);
	}
	
	/**
	 * @param index
	 * @param defaultValue
	 * @return the boolean
	 * @see com.jdroid.java.json.JSONArray#optBoolean(int, boolean)
	 */
	public boolean optBoolean(int index, boolean defaultValue) {
		return jsonArray.optBoolean(index, defaultValue);
	}
	
	/**
	 * @param index
	 * @return the double
	 * @see com.jdroid.java.json.JSONArray#optDouble(int)
	 */
	public double optDouble(int index) {
		return jsonArray.optDouble(index);
	}
	
	/**
	 * @param index
	 * @param defaultValue
	 * @return the double
	 * @see com.jdroid.java.json.JSONArray#optDouble(int, double)
	 */
	public double optDouble(int index, double defaultValue) {
		return jsonArray.optDouble(index, defaultValue);
	}
	
	/**
	 * @param index
	 * @return the int
	 * @see com.jdroid.java.json.JSONArray#optInt(int)
	 */
	public int optInt(int index) {
		return jsonArray.optInt(index);
	}
	
	/**
	 * @param index
	 * @param defaultValue
	 * @return the int
	 * @see com.jdroid.java.json.JSONArray#optInt(int, int)
	 */
	public int optInt(int index, int defaultValue) {
		return jsonArray.optInt(index, defaultValue);
	}
	
	/**
	 * @param index
	 * @return the {@link JsonArrayWrapper}
	 * @see com.jdroid.java.json.JSONArray#optJSONArray(int)
	 */
	public JsonArrayWrapper optJSONArray(int index) {
		return new JsonArrayWrapper(jsonArray.optJSONArray(index));
	}
	
	/**
	 * @param index
	 * @return the {@link JsonObjectWrapper}
	 * @see com.jdroid.java.json.JSONArray#optJSONObject(int)
	 */
	public JsonObjectWrapper optJSONObject(int index) {
		return new JsonObjectWrapper(jsonArray.optJSONObject(index));
	}
	
	/**
	 * @param index
	 * @return the long
	 * @see com.jdroid.java.json.JSONArray#optLong(int)
	 */
	public long optLong(int index) {
		return jsonArray.optLong(index);
	}
	
	/**
	 * @param index
	 * @param defaultValue
	 * @return the long
	 * @see com.jdroid.java.json.JSONArray#optLong(int, long)
	 */
	public long optLong(int index, long defaultValue) {
		return jsonArray.optLong(index, defaultValue);
	}
	
	/**
	 * @param index
	 * @return the String
	 * @see com.jdroid.java.json.JSONArray#optString(int)
	 */
	public String optString(int index) {
		return jsonArray.optString(index);
	}
	
	/**
	 * @param index
	 * @param defaultValue
	 * @return the {@link String}
	 * @see com.jdroid.java.json.JSONArray#optString(int, java.lang.String)
	 */
	public String optString(int index, String defaultValue) {
		return jsonArray.optString(index, defaultValue);
	}
	
	/**
	 * @param value
	 * @return the {@link JsonArrayWrapper}
	 * @see com.jdroid.java.json.JSONArray#put(boolean)
	 */
	public JsonArrayWrapper put(boolean value) {
		return new JsonArrayWrapper(jsonArray.put(value));
	}
	
	/**
	 * @param value
	 * @return the {@link JsonArrayWrapper}
	 * @see com.jdroid.java.json.JSONArray#put(double)
	 */
	public JsonArrayWrapper put(double value) {
		return new JsonArrayWrapper(jsonArray.put(value));
	}
	
	/**
	 * @param value
	 * @return the {@link JsonArrayWrapper}
	 * @see com.jdroid.java.json.JSONArray#put(int)
	 */
	public JsonArrayWrapper put(int value) {
		return new JsonArrayWrapper(jsonArray.put(value));
	}
	
	/**
	 * @param value
	 * @return the {@link JsonArrayWrapper}
	 * @see com.jdroid.java.json.JSONArray#put(long)
	 */
	public JsonArrayWrapper put(long value) {
		return new JsonArrayWrapper(jsonArray.put(value));
	}
	
	/**
	 * @param value
	 * @return the {@link JsonArrayWrapper}
	 * @see com.jdroid.java.json.JSONArray#put(java.lang.Object)
	 */
	public JsonArrayWrapper put(Object value) {
		return new JsonArrayWrapper(jsonArray.put(value));
	}
	
	/**
	 * @param index
	 * @param value
	 * @return {@link JsonArrayWrapper}
	 * @see com.jdroid.java.json.JSONArray#put(int, boolean)
	 */
	public JsonArrayWrapper put(int index, boolean value) {
		return new JsonArrayWrapper(jsonArray.put(index, value));
	}
	
	/**
	 * @param index
	 * @param value
	 * @return the {@link JsonArrayWrapper}
	 * @see com.jdroid.java.json.JSONArray#put(int, double)
	 */
	public JsonArrayWrapper put(int index, double value) {
		return new JsonArrayWrapper(jsonArray.put(index, value));
	}
	
	/**
	 * @param index
	 * @param value
	 * @return the {@link JsonArrayWrapper}
	 * @see com.jdroid.java.json.JSONArray#put(int, int)
	 */
	public JsonArrayWrapper put(int index, int value) {
		return new JsonArrayWrapper(jsonArray.put(index, value));
	}
	
	/**
	 * @param index
	 * @param value
	 * @return the {@link JsonArrayWrapper}
	 * @see com.jdroid.java.json.JSONArray#put(int, long)
	 */
	public JsonArrayWrapper put(int index, long value) {
		return new JsonArrayWrapper(jsonArray.put(index, value));
	}
	
	/**
	 * @param index
	 * @param value
	 * @return the {@link JsonArrayWrapper}
	 * @see com.jdroid.java.json.JSONArray#put(int, java.lang.Object)
	 */
	public JsonArrayWrapper put(int index, Object value) {
		return new JsonArrayWrapper(jsonArray.put(index, value));
	}
	
	/**
	 * @param names
	 * @return the {@link JsonObjectWrapper}
	 * @see com.jdroid.java.json.JSONArray#toJSONObject(com.jdroid.java.json.JSONArray)
	 */
	public JsonObjectWrapper toJSONObject(JSONArray names) {
		return new JsonObjectWrapper(jsonArray.toJSONObject(names));
	}
	
	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return jsonArray.toString();
	}
	
	/**
	 * @param indentFactor
	 * @return the {@link String}
	 * @see com.jdroid.java.json.JSONArray#toString(int)
	 */
	public String toString(int indentFactor) {
		return jsonArray.toString(indentFactor);
	}
	
}
