package com.jdroid.android.firebase.remoteconfig;

import com.google.firebase.remoteconfig.FirebaseRemoteConfigValue;
import com.jdroid.java.utils.TypeUtils;

import java.util.Map;

public class MockFirebaseRemoteConfigValue implements FirebaseRemoteConfigValue {

	private RemoteConfigParameter remoteConfigParameter;
	private Map<String, String> mocks;

	public MockFirebaseRemoteConfigValue(RemoteConfigParameter remoteConfigParameter, Map<String, String> mocks) {
		this.remoteConfigParameter = remoteConfigParameter;
		this.mocks = mocks;
	}

	@Override
	public long asLong() throws IllegalArgumentException {
		return TypeUtils.getLong(mocks.get(remoteConfigParameter.getKey()), 0L);
	}

	@Override
	public double asDouble() throws IllegalArgumentException {
		return TypeUtils.getDouble(mocks.get(remoteConfigParameter.getKey()), 0D);
	}

	@Override
	public String asString() {
		return mocks.get(remoteConfigParameter.getKey());
	}

	@Override
	public byte[] asByteArray() {
		String value = mocks.get(remoteConfigParameter.getKey());
		return value != null ? value.getBytes() : new byte[] {};
	}

	@Override
	public boolean asBoolean() throws IllegalArgumentException {
		return TypeUtils.getBoolean(mocks.get(remoteConfigParameter.getKey()), false);
	}

	@Override
	public int getSource() {
		return -1;
	}
}
