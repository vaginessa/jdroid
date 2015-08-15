package com.jdroid.android.sample.ui.toasts;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.jdroid.android.fragment.AbstractFragment;
import com.jdroid.android.sample.R;
import com.jdroid.android.utils.ToastUtils;

public class ToastsFragment extends AbstractFragment {
	
	@Override
	public Integer getContentFragmentLayout() {
		return R.layout.toasts_fragment;
	}

	/**
	 * @see com.jdroid.android.fragment.AbstractFragment#onViewCreated(android.view.View, android.os.Bundle)
	 */
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		findView(R.id.normalToast).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ToastUtils.showToast(R.string.normalToastMessage);
			}
		});
		findView(R.id.infoToast).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ToastUtils.showInfoToast(R.string.infoToastMessage);
			}
		});
		findView(R.id.warningToast).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ToastUtils.showWarningToast(R.string.warningToastMessage);
			}
		});
		findView(R.id.errorToast).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ToastUtils.showErrorToast(R.string.errorToastMessage);
			}
		});
	}
}
