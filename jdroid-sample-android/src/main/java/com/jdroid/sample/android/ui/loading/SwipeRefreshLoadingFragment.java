package com.jdroid.sample.android.ui.loading;

import android.os.Bundle;

import com.google.android.gms.ads.AdSize;
import com.jdroid.android.fragment.AbstractRecyclerFragment;
import com.jdroid.android.fragment.FragmentHelper.UseCaseTrigger;
import com.jdroid.android.loading.FragmentLoading;
import com.jdroid.android.loading.SwipeRefreshLoading;
import com.jdroid.sample.android.R;
import com.jdroid.sample.android.ui.adapter.SampleAdapter;
import com.jdroid.sample.android.usecase.SampleUseCase;

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

	@Override
	public AdSize getAdSize() {
		return null;
	}
}
