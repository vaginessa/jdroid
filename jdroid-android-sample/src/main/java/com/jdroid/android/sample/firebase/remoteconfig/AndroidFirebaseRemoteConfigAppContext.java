package com.jdroid.android.sample.firebase.remoteconfig;

import com.jdroid.android.firebase.remoteconfig.FirebaseRemoteConfigAppContext;
import com.jdroid.android.firebase.remoteconfig.RemoteConfigParameter;
import com.jdroid.java.collections.Lists;

import java.util.List;

public class AndroidFirebaseRemoteConfigAppContext extends FirebaseRemoteConfigAppContext {

	@Override
	public List<RemoteConfigParameter> getRemoteConfigParameters() {
		return Lists.<RemoteConfigParameter>newArrayList(AndroidRemoteConfigParameter.values());
	}
}
