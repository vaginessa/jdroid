package com.jdroid.android.sample.ui.leakcanary;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.jdroid.android.activity.FragmentContainerActivity;

public class LeakCanaryActivity extends FragmentContainerActivity {
	
	private static LeakCanaryActivity LEAK;
	
	@Override
	protected Class<? extends Fragment> getFragmentClass() {
		return LeakCanaryFragment.class;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		LEAK = this;
	}
}
