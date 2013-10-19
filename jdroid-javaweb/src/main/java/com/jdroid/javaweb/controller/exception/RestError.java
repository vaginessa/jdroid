package com.jdroid.javaweb.controller.exception;

import org.springframework.http.HttpStatus;
import org.springframework.util.ObjectUtils;

/**
 * @author Les Hazlewood
 */
public class RestError {
	
	private HttpStatus status;
	private String code;
	private String message;
	private String developerMessage;
	private Throwable throwable;
	
	public RestError(HttpStatus status, String code, String message, String developerMessage, Throwable throwable) {
		if (status == null) {
			throw new NullPointerException("HttpStatus argument cannot be null.");
		}
		this.status = status;
		this.code = code;
		this.message = message;
		this.developerMessage = developerMessage;
		this.throwable = throwable;
	}
	
	public HttpStatus getStatus() {
		return status;
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
	
	public Throwable getThrowable() {
		return throwable;
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o instanceof RestError) {
			RestError re = (RestError)o;
			return ObjectUtils.nullSafeEquals(getStatus(), re.getStatus()) && (getCode() == re.getCode())
					&& ObjectUtils.nullSafeEquals(getMessage(), re.getMessage())
					&& ObjectUtils.nullSafeEquals(getDeveloperMessage(), re.getDeveloperMessage())
					&& ObjectUtils.nullSafeEquals(getThrowable(), re.getThrowable());
		}
		
		return false;
	}
	
	@Override
	public int hashCode() {
		// noinspection ThrowableResultOfMethodCallIgnored
		return ObjectUtils.nullSafeHashCode(new Object[] { getStatus(), getCode(), getMessage(), getDeveloperMessage(),
				getThrowable() });
	}
	
	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "RestError [status=" + status + ", code=" + code + ", message=" + message + ", developerMessage="
				+ developerMessage + ", throwable=" + throwable + "]";
	}
	
	public static class Builder {
		
		private HttpStatus status;
		private String code;
		private String message;
		private String developerMessage;
		private Throwable throwable;
		
		public Builder setStatus(int statusCode) {
			status = HttpStatus.valueOf(statusCode);
			return this;
		}
		
		public Builder setStatus(HttpStatus status) {
			this.status = status;
			return this;
		}
		
		public Builder setCode(String code) {
			this.code = code;
			return this;
		}
		
		public Builder setMessage(String message) {
			this.message = message;
			return this;
		}
		
		public Builder setDeveloperMessage(String developerMessage) {
			this.developerMessage = developerMessage;
			return this;
		}
		
		public Builder setThrowable(Throwable throwable) {
			this.throwable = throwable;
			return this;
		}
		
		public RestError build() {
			if (status == null) {
				status = HttpStatus.INTERNAL_SERVER_ERROR;
			}
			return new RestError(status, code, message, developerMessage, throwable);
		}
	}
	
}