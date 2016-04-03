package com.jdroid.android.sample.ui.loading;

import android.os.Bundle;
import com.jdroid.android.fragment.AbstractFragment;
import com.jdroid.android.fragment.FragmentHelper.UseCaseTrigger;
import com.jdroid.android.sample.usecase.SampleItemsUseCase;

public class BlockingLoadingFragment extends AbstractFragment {
	
	private SampleItemsUseCase sampleItemsUseCase;
	
	/**
	 * @see com.jdroid.android.fragment.AbstractFragment#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		sampleItemsUseCase = new SampleItemsUseCase();
	}
	
	/**
	 * @see com.jdroid.android.fragment.AbstractFragment#onResume()
	 */
	@Override
	public void onResume() {
		super.onResume();
		onResumeUseCase(sampleItemsUseCase, this, UseCaseTrigger.ONCE);
	}
	
	/**
	 * @see com.jdroid.android.fragment.AbstractFragment#onPause()
	 */
	@Override
	public void onPause() {
		super.onPause();
		onPauseUseCase(sampleItemsUseCase, this);
	}
}
