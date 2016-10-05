package com.jdroid.android.usecase.listener;

/**
 * Use Case Listener that doesn't display a loading dialog
 */
public abstract class LoadinglessUseCaseListener extends FragmentUseCaseListener {
	
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
}
