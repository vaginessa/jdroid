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

		findView(R.id.matchError).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				ActivityLauncher.launchActivity(MatchErrorActivity.class);

			}
		});

		findView(R.id.mainIntentError).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				ActivityLauncher.launchActivity(MainIntentErrorActivity.class);

			}
		});

		findView(R.id.defaultIntentError).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				ActivityLauncher.launchActivity(DefaulItntentErrorActivity.class);

			}
		});

		findView(R.id.matchNewActivity).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				ActivityLauncher.launchActivity(MatchNewActivity.class);

			}
		});

		findView(R.id.noMatchNewActivity).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				ActivityLauncher.launchActivity(NoMatchNewActivity.class);

			}
		});

		findView(R.id.matchSameActivity).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				ActivityLauncher.launchActivity(MatchSameActivity.class);

			}
		});

		findView(R.id.noMatchSameActivity).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				ActivityLauncher.launchActivity(NoMatchSameActivity.class);

			}
		});

		findView(R.id.matchNullIntent).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				ActivityLauncher.launchActivity(MatchNullIntentActivity.class);

			}
		});

		findView(R.id.noMatchNullIntent).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				ActivityLauncher.launchActivity(NoMatchNullIntentActivity.class);

			}
		});
	}
}
