package com.jdroid.android.sample.ui.firebase.remoteconfig;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.jdroid.android.firebase.remoteconfig.FirebaseRemoteConfigHelper;
import com.jdroid.android.fragment.AbstractFragment;
import com.jdroid.android.sample.R;

public class FirebaseRemoteConfigFragment extends AbstractFragment {

	@Override
	public Integer getContentFragmentLayout() {
		return R.layout.firebase_remote_config_fragment;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		TextView sampleConfig1 = findView(R.id.sampleConfig1);
		sampleConfig1.setText(FirebaseRemoteConfigHelper.getFirebaseRemoteConfig().getString("sampleConfig1"));

		TextView sampleConfig2 = findView(R.id.sampleConfig2);
		sampleConfig2.setText(FirebaseRemoteConfigHelper.getFirebaseRemoteConfig().getString("sampleConfig2"));

		TextView sampleConfig3 = findView(R.id.sampleConfig3);
		sampleConfig3.setText(FirebaseRemoteConfigHelper.getFirebaseRemoteConfig().getString("sampleConfig3"));
	}
}
