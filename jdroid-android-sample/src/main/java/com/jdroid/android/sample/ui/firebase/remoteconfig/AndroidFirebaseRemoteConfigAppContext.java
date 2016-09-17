package com.jdroid.android.sample.ui.firebase.remoteconfig;

import com.jdroid.android.firebase.remoteconfig.FirebaseRemoteConfigAppContext;
import com.jdroid.android.firebase.remoteconfig.RemoteConfigParameter;
import com.jdroid.android.sample.experiment.AndroidRemoteConfigParameter;
import com.jdroid.java.collections.Lists;

import java.util.List;

public class AndroidFirebaseRemoteConfigAppContext extends FirebaseRemoteConfigAppContext {

	@Override
	public List<RemoteConfigParameter> getRemoteConfigParameters() {
		return Lists.<RemoteConfigParameter>newArrayList(AndroidRemoteConfigParameter.values());
	}
}
