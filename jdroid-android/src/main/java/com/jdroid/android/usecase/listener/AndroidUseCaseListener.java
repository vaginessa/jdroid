package com.jdroid.android.usecase.listener;

import com.jdroid.android.exception.DefaultExceptionHandler;
import com.jdroid.android.fragment.FragmentIf;
import com.jdroid.java.exception.AbstractException;

public abstract class AndroidUseCaseListener implements DefaultUseCaseListener {
	
	/**
	 * @see com.jdroid.android.usecase.listener.DefaultUseCaseListener#onStartUseCase()
	 */
	@Override
	public void onStartUseCase() {
		FragmentIf fragmentIf = getFragmentIf();
		if (fragmentIf != null) {
			fragmentIf.showLoading();
		}
	}
	
	/**
	 * @see com.jdroid.android.usecase.listener.DefaultUseCaseListener#onUpdateUseCase()
	 */
	@Override
	public void onUpdateUseCase() {
		getFragmentIf().onUpdateUseCase();
	}
	
	/**
	 * @see com.jdroid.android.usecase.listener.DefaultUseCaseListener#onFinishFailedUseCase(com.jdroid.java.exception.AbstractException)
	 */
	@Override
	public void onFinishFailedUseCase(AbstractException abstractException) {
		if (goBackOnError(abstractException)) {
			DefaultExceptionHandler.markAsGoBackOnError(abstractException);
		} else {
			DefaultExceptionHandler.markAsNotGoBackOnError(abstractException);
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
	 * @see com.jdroid.android.usecase.listener.DefaultUseCaseListener#onFinishUseCase()
	 */
	@Override
	public void onFinishUseCase() {
		getFragmentIf().onFinishUseCase();
	}
	
	protected abstract FragmentIf getFragmentIf();
}
