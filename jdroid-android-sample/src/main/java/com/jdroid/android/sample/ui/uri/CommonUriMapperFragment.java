package com.jdroid.android.sample.ui.uri;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.jdroid.android.fragment.AbstractFragment;
import com.jdroid.android.sample.R;

public class CommonUriMapperFragment extends AbstractFragment {

	@Override
	public Integer getContentFragmentLayout() {
		return R.layout.common_uri_mapper_fragment;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		initTextView();
	}

	@Override
	public void onNewIntent(Intent intent) {
		super.onNewIntent(intent);

		initTextView();
	}

	private void initTextView() {
		if (getActivity().getIntent().getData() != null) {
			((TextView)findView(R.id.uri)).setText(getActivity().getIntent().getData().toString());
		}
	}
}
