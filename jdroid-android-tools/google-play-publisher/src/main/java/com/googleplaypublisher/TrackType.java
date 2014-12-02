package com.googleplaypublisher;

public enum TrackType {
	
	ALPHA("alpha"),
	BETA("beta"),
	PRODUCTION("production"),
	ROLLOUT("rollout");
	
	private String name;
	
	private TrackType(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
}
