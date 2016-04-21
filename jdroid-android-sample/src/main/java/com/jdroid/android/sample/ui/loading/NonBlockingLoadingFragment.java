package com.jdroid.android.sample.ui.loading;

import android.os.Bundle;

import com.jdroid.android.fragment.AbstractFragment;
import com.jdroid.android.fragment.FragmentHelper.UseCaseTrigger;
import com.jdroid.android.loading.FragmentLoading;
import com.jdroid.android.loading.NonBlockingLoading;
import com.jdroid.android.sample.R;
import com.jdroid.android.sample.usecase.SampleItemsUseCase;

public class NonBlockingLoadingFragment extends AbstractFragment {
	
	private SampleItemsUseCase sampleItemsUseCase;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		sampleItemsUseCase = new SampleItemsUseCase();
	}

	@Override
	public Integer getContentFragmentLayout() {
		return R.layout.non_blocking_loading_fragment;
	}

	@Override
	public void onResume() {
		super.onResume();
		onResumeUseCase(sampleItemsUseCase, this, UseCaseTrigger.ONCE);
	}
	
	@Override
	public void onPause() {
		super.onPause();
		onPauseUseCase(sampleItemsUseCase, this);
	}
	
	@Override
	public FragmentLoading getDefaultLoading() {
		return new NonBlockingLoading();
	}
}
