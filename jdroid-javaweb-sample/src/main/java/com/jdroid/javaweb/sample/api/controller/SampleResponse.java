package com.jdroid.javaweb.sample.api.controller;

public class SampleResponse {

	private String sampleKey;

	public SampleResponse(String sampleKey) {
		this.sampleKey = sampleKey;
	}

	public String getSampleKey() {
		return sampleKey;
	}
}