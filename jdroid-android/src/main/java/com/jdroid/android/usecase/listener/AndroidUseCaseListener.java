package com.jdroid.android.usecase.listener;

import com.jdroid.android.exception.ErrorDisplayer;
import com.jdroid.android.fragment.FragmentIf;
import com.jdroid.java.exception.AbstractException;

public abstract class AndroidUseCaseListener implements UseCaseListener {
	
	@Override
	public void onStartUseCase() {
		FragmentIf fragmentIf = getFragmentIf();
		if (fragmentIf != null) {
			fragmentIf.showLoading();
		}
	}
	
	@Override
	public void onUpdateUseCase() {
		getFragmentIf().onUpdateUseCase();
	}
	
	@Override
	public void onFinishFailedUseCase(AbstractException abstractException) {
		FragmentIf fragmentIf = getFragmentIf();
		if (fragmentIf != null) {
			fragmentIf.dismissLoading();
			createErrorDisplayer(abstractException).displayError(abstractException);
		}
	}

	public ErrorDisplayer createErrorDisplayer(AbstractException abstractException) {
		return getFragmentIf().createErrorDisplayer(abstractException);
	}
	

	@Override
	public void onFinishUseCase() {
		getFragmentIf().onFinishUseCase();
	}
	
	protected abstract FragmentIf getFragmentIf();
}
