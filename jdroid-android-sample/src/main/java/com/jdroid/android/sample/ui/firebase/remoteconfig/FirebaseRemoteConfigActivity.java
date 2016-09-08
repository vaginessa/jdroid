package com.jdroid.android.sample.ui.firebase.remoteconfig;

import android.support.v4.app.Fragment;

import com.jdroid.android.activity.FragmentContainerActivity;

public class FirebaseRemoteConfigActivity extends FragmentContainerActivity {

	@Override
	protected Class<? extends Fragment> getFragmentClass() {
		return FirebaseRemoteConfigFragment.class;
	}
}