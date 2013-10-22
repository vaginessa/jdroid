package com.jdroid.javaweb.api;

import org.springframework.http.HttpStatus;

public class ApiError {
	
	private HttpStatus status;
	private String code;
	private String developerMessage;
	private String message;
	
	public ApiError(HttpStatus status, String code) {
		this(status, code, null, null);
	}
	
	public ApiError(HttpStatus status, String code, String developerMessage) {
		this(status, code, developerMessage, null);
	}
	
	public ApiError(HttpStatus status, String code, String developerMessage, String message) {
		if (status == null) {
			throw new NullPointerException("HttpStatus argument cannot be null.");
		}
		this.status = status;
		
		if (code == null) {
			this.code = "500";
		} else {
			this.code = code;
		}
		this.developerMessage = developerMessage;
		this.message = message;
	}
	
	public int getStatus() {
		return status.value();
	}
	
	public String getCode() {
		return code;
	}
	
	public String getMessage() {
		return message;
	}
	
	public String getDeveloperMessage() {
		return developerMessage;
	}
	
	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ApiError [status=" + status + ", code=" + code + ", message=" + message + ", developerMessage="
				+ developerMessage + "]";
	}
	
}