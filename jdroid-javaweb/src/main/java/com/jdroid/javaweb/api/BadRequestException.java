package com.jdroid.javaweb.api;

import java.util.Map;
import com.jdroid.java.exception.ErrorCodeException;
import com.jdroid.javaweb.exception.CommonErrorCode;

@SuppressWarnings("rawtypes")
public class BadRequestException extends ErrorCodeException {
	
	private String uri;
	private Map uriParameters;
	private String requestMethod;
	private String servletName;
	
	public BadRequestException(String message, String uri, Map uriParameters, String requestMethod, String servletName) {
		super(CommonErrorCode.BAD_REQUEST, message);
		this.uri = uri;
		this.uriParameters = uriParameters;
		this.requestMethod = requestMethod;
		this.servletName = servletName;
	}
	
	/**
	 * @return the uri
	 */
	public String getUri() {
		return uri;
	}
	
	/**
	 * @param uri the uri to set
	 */
	public void setUri(String uri) {
		this.uri = uri;
	}
	
	/**
	 * @return the uriParameters
	 */
	public Map getUriParameters() {
		return uriParameters;
	}
	
	/**
	 * @param uriParameters the uriParameters to set
	 */
	public void setUriParameters(Map uriParameters) {
		this.uriParameters = uriParameters;
	}
	
	/**
	 * @return the requestMethod
	 */
	public String getRequestMethod() {
		return requestMethod;
	}
	
	/**
	 * @param requestMethod the requestMethod to set
	 */
	public void setRequestMethod(String requestMethod) {
		this.requestMethod = requestMethod;
	}
	
	/**
	 * @return the servletName
	 */
	public String getServletName() {
		return servletName;
	}
	
	/**
	 * @param servletName the servletName to set
	 */
	public void setServletName(String servletName) {
		this.servletName = servletName;
	}
	
}
