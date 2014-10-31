package com.jdroid.java.exception;

import java.util.Map;
import com.jdroid.java.collections.Maps;

public abstract class AbstractException extends RuntimeException {
	
	private static final long serialVersionUID = 6296155655850331666L;
	
	private Map<String, Object> parameters = Maps.newHashMap();
	private String title;
	private String description;
	private Boolean trackable = true;
	
	public AbstractException() {
		super();
	}
	
	public AbstractException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public AbstractException(String message) {
		super(message);
	}
	
	/**
	 * @param cause
	 */
	public AbstractException(Throwable cause) {
		super(cause);
	}
	
	public Map<String, Object> getParameters() {
		return parameters;
	}
	
	public Boolean hasParameter(String key) {
		return parameters.containsKey(key);
	}
	
	@SuppressWarnings("unchecked")
	public <E> E getParameter(String key) {
		return (E)parameters.get(key);
	}
	
	public void addParameter(String key, Object value) {
		parameters.put(key, value);
	}
	
	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}
	
	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	public Boolean isTrackable() {
		return trackable;
	}
	
	public void setTrackable(Boolean trackable) {
		this.trackable = trackable;
	}
	
	public Throwable getThrowableToLog() {
		return this;
	}
}
