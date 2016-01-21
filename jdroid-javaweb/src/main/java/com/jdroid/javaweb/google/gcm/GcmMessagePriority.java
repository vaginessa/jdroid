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

	public static GcmMessagePriority findByParameter(String parameter) {
		for (GcmMessagePriority each : values()) {
			if (each.getParameter().equals(parameter)) {
				return each;
			}
		}
		return null;
	}
}
