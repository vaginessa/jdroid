package com.jdroid.android.sample.ui.loading;

import android.os.Bundle;

import com.jdroid.android.fragment.FragmentHelper.UseCaseTrigger;
import com.jdroid.android.loading.FragmentLoading;
import com.jdroid.android.loading.SwipeRefreshLoading;
import com.jdroid.android.recycler.AbstractRecyclerFragment;
import com.jdroid.android.sample.R;
import com.jdroid.android.sample.ui.adapter.SampleAdapter;
import com.jdroid.android.sample.usecase.SampleUseCase;

public class SwipeRefreshLoadingFragment extends AbstractRecyclerFragment<String> {
	
	private SampleUseCase sampleUseCase;
	
	/**
	 * @see com.jdroid.android.fragment.AbstractFragment#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		sampleUseCase = getInstance(SampleUseCase.class);
	}
	
	@Override
	public Integer getContentFragmentLayout() {
		return R.layout.swipe_recycler_fragment;
	}

	/**
	 * @see com.jdroid.android.fragment.AbstractFragment#onResume()
	 */
	@Override
	public void onResume() {
		super.onResume();
		onResumeUseCase(sampleUseCase, this, UseCaseTrigger.ONCE);
	}
	
	/**
	 * @see com.jdroid.android.fragment.AbstractFragment#onPause()
	 */
	@Override
	public void onPause() {
		super.onPause();
		onPauseUseCase(sampleUseCase, this);
	}
	
	/**
	 * @see com.jdroid.android.fragment.AbstractFragment#getDefaultLoading()
	 */
	@Override
	public FragmentLoading getDefaultLoading() {
		return new SwipeRefreshLoading();
	}
	
	/**
	 * @see com.jdroid.android.fragment.AbstractListFragment#onRefresh()
	 */
	@Override
	public void onRefresh() {
		if (!sampleUseCase.isInProgress()) {
			executeUseCase(sampleUseCase);
		}
	}

	@Override
	public void onFinishUseCase() {
		executeOnUIThread(new Runnable() {
			@Override
			public void run() {
				setAdapter(new SampleAdapter(sampleUseCase.getItems()));
				dismissLoading();
			}
		});
	}
}
