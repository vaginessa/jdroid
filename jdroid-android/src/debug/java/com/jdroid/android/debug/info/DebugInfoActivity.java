package com.jdroid.android.debug.info;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.Fragment;

import com.jdroid.android.activity.FragmentContainerActivity;
import com.jdroid.android.firebase.instanceid.InstanceIdHelper;
import com.jdroid.java.concurrent.ExecutorUtils;

public class DebugInfoActivity extends FragmentContainerActivity {

	@Override
	public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
		super.onCreate(savedInstanceState, persistentState);

		// Load properties on a worker thread
		ExecutorUtils.execute(new Runnable() {
			@Override
			public void run() {
				InstanceIdHelper.getInstanceId();
			}
		});
	}

	@Override
	protected Class<? extends Fragment> getFragmentClass() {
		return DebugInfoFragment.class;
	}
}
