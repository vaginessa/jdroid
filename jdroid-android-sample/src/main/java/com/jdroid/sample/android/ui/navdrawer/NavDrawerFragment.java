package com.jdroid.sample.android.ui.navdrawer;

import android.os.Bundle;
import android.view.View;

import com.jdroid.android.activity.ActivityLauncher;
import com.jdroid.android.fragment.AbstractFragment;
import com.jdroid.sample.android.R;

public class NavDrawerFragment extends AbstractFragment {

	@Override
	public Integer getContentFragmentLayout() {
		return R.layout.navdrawer_fragment;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		findView(R.id.leftCustomNavDrawer).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ActivityLauncher.launchActivity(LeftCustomNavDrawerActivity.class);
			}
		});
		findView(R.id.rightCustomNavDrawer).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ActivityLauncher.launchActivity(RightCustomNavDrawerActivity.class);
			}
		});
		findView(R.id.userNavDrawer).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ActivityLauncher.launchActivity(UserNavDrawerActivity.class);
			}
		});
		findView(R.id.noNavDrawer).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ActivityLauncher.launchActivity(NoNavDrawerActivity.class);
			}
		});
	}
}
