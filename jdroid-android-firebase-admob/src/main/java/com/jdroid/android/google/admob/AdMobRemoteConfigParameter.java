package com.jdroid.android.google.admob;

import com.jdroid.android.firebase.remoteconfig.RemoteConfigParameter;

public enum AdMobRemoteConfigParameter implements RemoteConfigParameter {

	ADS_ENABLED(false),
	ADMOB_APP_ID,
	DEFAULT_AD_UNIT_ID;

	private Object defaultValue;

	AdMobRemoteConfigParameter() {
		this(null);
	}

	AdMobRemoteConfigParameter(Object defaultValue) {
		this.defaultValue = defaultValue;
	}

	@Override
	public String getKey() {
		return name();
	}

	@Override
	public Object getDefaultValue() {
		return AdMobAppModule.get().getAdMobAppContext().getBuildConfigValue(name(), defaultValue);
	}

	@Override
	public Boolean isUserProperty() {
		return false;
	}
}
