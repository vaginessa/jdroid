package com.jdroid.android.sample.ui.uri;

import android.os.Bundle;
import android.view.View;

import com.jdroid.android.activity.ActivityLauncher;
import com.jdroid.android.fragment.AbstractFragment;
import com.jdroid.android.sample.R;

public class UriMapperFragment extends AbstractFragment {

	@Override
	public Integer getContentFragmentLayout() {
		return R.layout.uri_mapper_fragment;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		findView(R.id.singleTop).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				ActivityLauncher.launchActivity(UriMapperSingleTopActivity.class);

			}
		});

		findView(R.id.noFlags).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				ActivityLauncher.launchActivity(UriMapperNoFlagsActivity.class);

			}
		});
	}
}
