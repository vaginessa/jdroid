package com.jdroid.android.experiments;

public enum SimpleExperimentVariant implements ExperimentHelper.ExperimentVariant {

	A("A"),
	B("B");

	private String id;

	SimpleExperimentVariant(String id) {
		this.id = id;
	}

	@Override
	public String getId() {
		return id;
	}
}
