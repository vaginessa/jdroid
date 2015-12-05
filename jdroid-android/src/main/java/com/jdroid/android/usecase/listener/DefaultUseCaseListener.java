package com.jdroid.android.usecase.listener;

import android.support.annotation.WorkerThread;

import com.jdroid.java.exception.AbstractException;

/**
 * Default Use Case Listener
 */
public interface DefaultUseCaseListener {
	
	/**
	 * Called before the use case starts
	 */
	@WorkerThread
	public void onStartUseCase();
	
	/**
	 * Called after the use case starts to report an update status if necessary
	 */
	@WorkerThread
	public void onUpdateUseCase();
	
	/**
	 * Called after the use case fails
	 * 
	 * @param abstractException The {@link AbstractException} with the error
	 */
	@WorkerThread
	public void onFinishFailedUseCase(AbstractException abstractException);
	
	/**
	 * Called when the use case finishes successfully
	 */
	@WorkerThread
	public void onFinishUseCase();
	
}
