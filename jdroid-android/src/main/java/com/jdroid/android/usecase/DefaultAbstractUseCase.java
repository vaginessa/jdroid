package com.jdroid.android.usecase;

import com.jdroid.android.usecase.listener.DefaultUseCaseListener;
import com.jdroid.java.exception.AbstractException;

/**
 * Abstract use case that handles the calls to {@link DefaultUseCaseListener#onStartUseCase()},
 * {@link DefaultUseCaseListener#onFinishUseCase()} and
 * {@link DefaultUseCaseListener#onFinishFailedUseCase(AbstractException)} when executing.
 */
public abstract class DefaultAbstractUseCase extends AbstractUseCase<DefaultUseCaseListener> {
	
	private static final long serialVersionUID = -6915799187802671145L;
	
	/**
	 * @see com.jdroid.android.usecase.AbstractUseCase#notifyFailedUseCase(com.jdroid.java.exception.AbstractException,
	 *      java.lang.Object)
	 */
	@Override
	protected void notifyFailedUseCase(AbstractException e, DefaultUseCaseListener listener) {
		listener.onFinishFailedUseCase(e);
	}
	
	/**
	 * @see com.jdroid.android.usecase.AbstractUseCase#notifyFinishedUseCase(java.lang.Object)
	 */
	@Override
	protected void notifyFinishedUseCase(DefaultUseCaseListener listener) {
		listener.onFinishUseCase();
	}
	
	/**
	 * @see com.jdroid.android.usecase.AbstractUseCase#notifyUseCaseStart(java.lang.Object)
	 */
	@Override
	protected void notifyUseCaseStart(DefaultUseCaseListener listener) {
		listener.onStartUseCase();
	}
}
