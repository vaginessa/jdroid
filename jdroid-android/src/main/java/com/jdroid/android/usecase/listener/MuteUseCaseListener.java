package com.jdroid.android.usecase.listener;

import com.jdroid.android.application.AbstractApplication;
import com.jdroid.java.exception.AbstractException;

/**
 * Default Use Case Listener that doesn't display loading or error dialogs
 */
public class MuteUseCaseListener implements UseCaseListener {
	
	/**
	 * @see UseCaseListener#onStartUseCase()
	 */
	@Override
	public void onStartUseCase() {
		// Do Nothing
	}
	
	/**
	 * @see UseCaseListener#onUpdateUseCase()
	 */
	@Override
	public void onUpdateUseCase() {
		// Do Nothing
	}
	
	/**
	 * @see UseCaseListener#onFinishUseCase()
	 */
	@Override
	public void onFinishUseCase() {
		// Do Nothing
		
	}
	
	/**
	 * @see UseCaseListener#onFinishFailedUseCase(com.jdroid.java.exception.AbstractException)
	 */
	@Override
	public void onFinishFailedUseCase(AbstractException abstractException) {
		AbstractApplication.get().getExceptionHandler().logHandledException(abstractException);
	}
}
