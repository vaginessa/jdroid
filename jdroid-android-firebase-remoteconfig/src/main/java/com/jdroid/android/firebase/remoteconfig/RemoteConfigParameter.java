package com.jdroid.android.firebase.remoteconfig;

public interface RemoteConfigParameter {

	String getKey();

	Object getDefaultValue();

	Boolean isUserProperty();
}
