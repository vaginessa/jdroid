package com.jdroid.android.sample.ui.fab;

import android.os.Bundle;
import android.view.View;

import com.jdroid.android.fragment.AbstractFragment;
import com.jdroid.android.sample.R;
import com.jdroid.android.snackbar.SnackbarBuilder;

public class FabFragment extends AbstractFragment {

	@Override
	public Integer getContentFragmentLayout() {
		return R.layout.fab_fragment;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		findView(R.id.fab).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				SnackbarBuilder snackbarBuilder = new SnackbarBuilder();
				snackbarBuilder.setDescription(R.string.ok);
				snackbarBuilder.build(getActivity()).show();
			}
		});
	}
}
