package com.jdroid.android.sample.ui.loading;

import android.os.Bundle;

import com.jdroid.android.fragment.AbstractFragment;
import com.jdroid.android.fragment.FragmentHelper.UseCaseTrigger;
import com.jdroid.android.loading.FragmentLoading;
import com.jdroid.android.loading.NonBlockingLoading;
import com.jdroid.android.sample.R;
import com.jdroid.android.sample.usecase.SampleUseCase;

public class NonBlockingLoadingFragment extends AbstractFragment {
	
	private SampleUseCase sampleUseCase;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		sampleUseCase = getInstance(SampleUseCase.class);
	}

	@Override
	public Integer getContentFragmentLayout() {
		return R.layout.non_bloacking_loading_fragment;
	}

	@Override
	public void onResume() {
		super.onResume();
		onResumeUseCase(sampleUseCase, this, UseCaseTrigger.ONCE);
	}
	
	@Override
	public void onPause() {
		super.onPause();
		onPauseUseCase(sampleUseCase, this);
	}
	
	@Override
	public FragmentLoading getDefaultLoading() {
		return new NonBlockingLoading();
	}
}
