package com.jdroid.sample.android.ui.toasts;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import com.jdroid.android.fragment.AbstractFragment;
import com.jdroid.android.utils.ToastUtils;
import com.jdroid.sample.android.R;

public class ToastsFragment extends AbstractFragment {
	
	/**
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup,
	 *      android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.toasts_fragment, container, false);
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
