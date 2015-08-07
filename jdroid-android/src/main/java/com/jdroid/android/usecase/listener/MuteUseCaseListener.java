package com.jdroid.android.usecase.listener;

import com.jdroid.android.application.AbstractApplication;
import com.jdroid.java.exception.AbstractException;

/**
 * Default Use Case Listener that doesn't display loading or error dialogs
 */
public class MuteUseCaseListener implements DefaultUseCaseListener {
	
	/**
	 * @see com.jdroid.android.usecase.listener.DefaultUseCaseListener#onStartUseCase()
	 */
	@Override
	public void onStartUseCase() {
		// Do Nothing
	}
	
	/**
	 * @see com.jdroid.android.usecase.listener.DefaultUseCaseListener#onUpdateUseCase()
	 */
	@Override
	public void onUpdateUseCase() {
		// Do Nothing
	}
	
	/**
	 * @see com.jdroid.android.usecase.listener.DefaultUseCaseListener#onFinishUseCase()
	 */
	@Override
	public void onFinishUseCase() {
		// Do Nothing
		
	}
	
	/**
	 * @see com.jdroid.android.usecase.listener.DefaultUseCaseListener#onFinishFailedUseCase(com.jdroid.java.exception.AbstractException)
	 */
	@Override
	public void onFinishFailedUseCase(AbstractException abstractException) {
		AbstractApplication.get().getExceptionHandler().logHandledException(abstractException);
	}
}
