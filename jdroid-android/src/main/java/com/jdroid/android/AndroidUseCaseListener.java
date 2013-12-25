package com.jdroid.android;

import com.jdroid.android.exception.DefaultExceptionHandler;
import com.jdroid.android.fragment.FragmentIf;
import com.jdroid.android.usecase.listener.DefaultUseCaseListener;

/**
 * 
 * @author Maxi Rosson
 */
public abstract class AndroidUseCaseListener implements DefaultUseCaseListener {
	
	/**
	 * @see com.jdroid.android.usecase.listener.DefaultUseCaseListener#onStartUseCase()
	 */
	@Override
	public void onStartUseCase() {
		getFragmentIf().onStartUseCase();
	}
	
	/**
	 * @see com.jdroid.android.usecase.listener.DefaultUseCaseListener#onUpdateUseCase()
	 */
	@Override
	public void onUpdateUseCase() {
		getFragmentIf().onUpdateUseCase();
	}
	
	/**
	 * @see com.jdroid.android.usecase.listener.DefaultUseCaseListener#onFinishFailedUseCase(java.lang.RuntimeException)
	 */
	@Override
	public void onFinishFailedUseCase(RuntimeException runtimeException) {
		getFragmentIf().onFinishFailedUseCase(runtimeException);
		if (goBackOnError(runtimeException)) {
			DefaultExceptionHandler.markAsGoBackOnError(runtimeException);
		} else {
			DefaultExceptionHandler.markAsNotGoBackOnError(runtimeException);
		}
		FragmentIf fragmentIf = getFragmentIf();
		if (fragmentIf.isBlockingLoadingEnabled()) {
			fragmentIf.dismissBlockingLoading();
		} else {
			fragmentIf.dismissNonBlockingLoading();
		}
		
		throw runtimeException;
	}
	
	public Boolean goBackOnError(RuntimeException runtimeException) {
		FragmentIf fragmentIf = getFragmentIf();
		if (fragmentIf != null) {
			return fragmentIf.goBackOnError(runtimeException);
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
	
	/**
	 * @see com.jdroid.android.usecase.listener.DefaultUseCaseListener#onFinishCanceledUseCase()
	 */
	@Override
	public void onFinishCanceledUseCase() {
		getFragmentIf().onFinishCanceledUseCase();
	}
	
	protected abstract FragmentIf getFragmentIf();
}
