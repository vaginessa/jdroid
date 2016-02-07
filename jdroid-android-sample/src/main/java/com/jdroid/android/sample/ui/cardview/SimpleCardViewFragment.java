package com.jdroid.android.sample.ui.cardview;

import android.os.Bundle;
import android.view.View;

import com.jdroid.android.fragment.AbstractFragment;
import com.jdroid.android.sample.R;

public class SimpleCardViewFragment extends AbstractFragment {

	@Override
	public Integer getContentFragmentLayout() {
		return R.layout.simple_cardview_fragment;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		findView(R.id.clickableCardView).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// Do nothing
			}
		});
	}
}
