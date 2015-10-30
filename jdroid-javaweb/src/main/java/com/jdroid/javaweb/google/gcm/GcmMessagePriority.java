package com.jdroid.javaweb.google.gcm;

public enum GcmMessagePriority {

	NORMAL("normal"),
	HIGH("high");

	private String parameter;

	GcmMessagePriority(String parameter) {
		this.parameter = parameter;
	}

	public String getParameter() {
		return parameter;
	}
}
