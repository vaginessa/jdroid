package com.jdroid.android.usecase.listener;

import com.jdroid.android.exception.DialogErrorDisplayer;
import com.jdroid.android.fragment.FragmentIf;
import com.jdroid.java.exception.AbstractException;

public abstract class AndroidUseCaseListener implements UseCaseListener {
	
	/**
	 * @see UseCaseListener#onStartUseCase()
	 */
	@Override
	public void onStartUseCase() {
		FragmentIf fragmentIf = getFragmentIf();
		if (fragmentIf != null) {
			fragmentIf.showLoading();
		}
	}
	
	/**
	 * @see UseCaseListener#onUpdateUseCase()
	 */
	@Override
	public void onUpdateUseCase() {
		getFragmentIf().onUpdateUseCase();
	}
	
	/**
	 * @see UseCaseListener#onFinishFailedUseCase(com.jdroid.java.exception.AbstractException)
	 */
	@Override
	public void onFinishFailedUseCase(AbstractException abstractException) {
		if (goBackOnError(abstractException)) {
			DialogErrorDisplayer.markAsGoBackOnError(abstractException);
		} else {
			DialogErrorDisplayer.markAsNotGoBackOnError(abstractException);
		}
		FragmentIf fragmentIf = getFragmentIf();
		if (fragmentIf != null) {
			fragmentIf.dismissLoading();
		}
		
		throw abstractException;
	}
	
	public Boolean goBackOnError(AbstractException abstractException) {
		FragmentIf fragmentIf = getFragmentIf();
		if (fragmentIf != null) {
			return fragmentIf.goBackOnError(abstractException);
		} else {
			return true;
		}
	}
	
	/**
	 * @see UseCaseListener#onFinishUseCase()
	 */
	@Override
	public void onFinishUseCase() {
		getFragmentIf().onFinishUseCase();
	}
	
	protected abstract FragmentIf getFragmentIf();
}
