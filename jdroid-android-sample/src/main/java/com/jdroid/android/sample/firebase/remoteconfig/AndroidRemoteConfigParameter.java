package com.jdroid.android.sample.firebase.remoteconfig;

import com.jdroid.android.firebase.remoteconfig.RemoteConfigParameter;

public enum AndroidRemoteConfigParameter implements RemoteConfigParameter {

	SAMPLE_CONFIG_1("defaultConfigValue1"),
	SAMPLE_CONFIG_2("default", true),
	SAMPLE_CONFIG_3(null);

	private Object defaultValue;
	private Boolean isABTestingExperiment;

	AndroidRemoteConfigParameter(Object defaultValue) {
		this(defaultValue, false);
	}

	AndroidRemoteConfigParameter(Object defaultValue, Boolean isABTestingExperiment) {
		this.defaultValue = defaultValue;
		this.isABTestingExperiment = isABTestingExperiment;
	}

	@Override
	public String getKey() {
		return name();
	}

	@Override
	public Object getDefaultValue() {
		return defaultValue;
	}

	@Override
	public Boolean isUserProperty() {
		return isABTestingExperiment;
	}
}
