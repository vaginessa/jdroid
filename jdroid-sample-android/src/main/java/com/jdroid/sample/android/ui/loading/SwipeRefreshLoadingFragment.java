package com.jdroid.sample.android.ui.loading;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.jdroid.android.fragment.AbstractListFragment;
import com.jdroid.android.fragment.FragmentHelper.UseCaseTrigger;
import com.jdroid.android.loading.FragmentLoading;
import com.jdroid.android.loading.SwipeRefreshLoading;
import com.jdroid.sample.android.R;
import com.jdroid.sample.android.usecase.SampleUseCase;

public class SwipeRefreshLoadingFragment extends AbstractListFragment<Object> {
	
	private SampleUseCase sampleUseCase;
	
	/**
	 * @see com.jdroid.android.fragment.AbstractFragment#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		sampleUseCase = getInstance(SampleUseCase.class);
	}
	
	/**
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup,
	 *      android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.swipe_list_fragment, container, false);
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
		SwipeRefreshLoading swipeRefreshLoading = new SwipeRefreshLoading();
		swipeRefreshLoading.setColorRes1(R.color.blue);
		swipeRefreshLoading.setColorRes2(R.color.grey);
		swipeRefreshLoading.setColorRes3(R.color.lightBlue);
		swipeRefreshLoading.setColorRes4(android.R.color.white);
		return swipeRefreshLoading;
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
}
