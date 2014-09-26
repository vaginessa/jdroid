package com.jdroid.sample.android.ui.loading;

import android.os.Bundle;
import com.jdroid.android.activity.ActivityIf;
import com.jdroid.android.fragment.AbstractFragment;
import com.jdroid.android.fragment.FragmentHelper.UseCaseTrigger;
import com.jdroid.android.loading.ActivityLoading;
import com.jdroid.android.utils.ToastUtils;
import com.jdroid.sample.android.R;
import com.jdroid.sample.android.usecase.SampleUseCase;

public class CustomActivityLoadingFragment extends AbstractFragment {
	
	private SampleUseCase sampleUseCase;
	
	/**
	 * @see com.jdroid.android.fragment.AbstractFragment#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		sampleUseCase = getInstance(SampleUseCase.class);
		getActivityIf().setLoading(new ActivityLoading() {
			
			@Override
			public void show(ActivityIf activityIf) {
				ToastUtils.showToast(R.string.showLoading);
				
			}
			
			@Override
			public void dismiss(ActivityIf activityIf) {
				ToastUtils.showToast(R.string.dismissLoading);
			}
		});
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
}
