package com.jdroid.android.usecase.listener;

import com.jdroid.android.exception.ErrorDisplayer;
import com.jdroid.android.fragment.FragmentIf;
import com.jdroid.java.exception.AbstractException;

public abstract class ActivityLoadingUseCaseListener implements UseCaseListener {
	
	@Override
	public void onStartUseCase() {
		FragmentIf fragmentIf = getFragmentIf();
		if (fragmentIf != null) {
			fragmentIf.getActivityIf().showLoading();
		}
	}
	
	@Override
	public void onUpdateUseCase() {
		FragmentIf fragmentIf = getFragmentIf();
		if (fragmentIf != null) {
			fragmentIf.onUpdateUseCase();
		}
	}
	
	@Override
	public void onFinishFailedUseCase(AbstractException abstractException) {
		FragmentIf fragmentIf = getFragmentIf();
		if (fragmentIf != null) {
			fragmentIf.getActivityIf().dismissLoading();
			createErrorDisplayer(abstractException).displayError(abstractException);
		}
	}

	public ErrorDisplayer createErrorDisplayer(AbstractException abstractException) {
		return getFragmentIf().createErrorDisplayer(abstractException);
	}
	

	@Override
	public void onFinishUseCase() {
		FragmentIf fragmentIf = getFragmentIf();
		if (fragmentIf != null) {
			fragmentIf.getActivityIf().dismissLoading();
		}
	}
	
	protected abstract FragmentIf getFragmentIf();
}
