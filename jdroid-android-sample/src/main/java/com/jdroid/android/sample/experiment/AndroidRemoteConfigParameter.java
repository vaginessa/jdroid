package com.jdroid.android.sample.experiment;

import com.jdroid.android.firebase.remoteconfig.RemoteConfigParameter;

public enum AndroidRemoteConfigParameter implements RemoteConfigParameter {

	SAMPLE_CONFIG_1("sampleConfig1", "defaultConfigValue1"),
	SAMPLE_CONFIG_2("sampleConfig2", "default", true),
	SAMPLE_CONFIG_3("sampleConfig3", "defaultConfigValue3");

	private String key;
	private Object defaultValue;
	private Boolean isABTestingExperiment;

	AndroidRemoteConfigParameter(String key, Object defaultValue) {
		this(key, defaultValue, false);
	}

	AndroidRemoteConfigParameter(String key, Object defaultValue, Boolean isABTestingExperiment) {
		this.key = key;
		this.defaultValue = defaultValue;
		this.isABTestingExperiment = isABTestingExperiment;
	}

	@Override
	public String getKey() {
		return key;
	}

	@Override
	public Object getDefaultValue() {
		return defaultValue;
	}

	@Override
	public Boolean isABTestingExperiment() {
		return isABTestingExperiment;
	}
}
