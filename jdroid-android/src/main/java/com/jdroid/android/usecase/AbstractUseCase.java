package com.jdroid.android.usecase;

import android.support.annotation.WorkerThread;

import com.jdroid.android.application.AbstractApplication;
import com.jdroid.java.collections.Lists;
import com.jdroid.java.concurrent.ExecutorUtils;
import com.jdroid.java.exception.AbstractException;
import com.jdroid.java.exception.UnexpectedException;
import com.jdroid.java.date.DateUtils;
import com.jdroid.java.utils.LoggerUtils;

import org.slf4j.Logger;

import java.io.Serializable;
import java.util.List;

/**
 * 
 * @param <T> The Listener type
 */
public abstract class AbstractUseCase<T> implements UseCase<T>, Serializable {
	
	private static final long serialVersionUID = 3732327346852606739L;
	
	private final static Logger LOGGER = LoggerUtils.getLogger(AbstractUseCase.class);
	
	public enum UseCaseStatus {
		NOT_INVOKED,
		IN_PROGRESS,
		FINISHED_SUCCESSFUL,
		FINISHED_FAILED;
	}
	
	private List<T> listeners = Lists.newArrayList();
	private UseCaseStatus useCaseStatus = UseCaseStatus.NOT_INVOKED;
	private AbstractException abstractException;
	private Boolean notified = false;
	
	private Long executionTime = 0L;
	private Long minimumExecutionTime = 0L;
	private int exceptionPriorityLevel = AbstractException.NORMAL_PRIORITY;

	/**
	 * Executes the use case.
	 */
	@WorkerThread
	@Override
	public final void run() {
		
		LOGGER.debug("Executing " + getClass().getSimpleName());
		markAsInProgress();
		for (T listener : listeners) {
			notifyUseCaseStart(listener);
		}
		try {
			
			LOGGER.debug("Started use case " + getClass().getSimpleName());
			long startTime = System.currentTimeMillis();
			doExecute();
			executionTime = System.currentTimeMillis() - startTime;
			LOGGER.debug("Finished use case " + getClass().getSimpleName() + ". Execution time: "
					+ DateUtils.formatDuration(executionTime));
			
			if (executionTime < minimumExecutionTime) {
				ExecutorUtils.sleepInMillis(minimumExecutionTime - executionTime);
				LOGGER.debug("Delaying the use case to achieve the minimum execution time of " + minimumExecutionTime
						+ "ms");
			}
			
			markAsSuccessful();
			for (T listener : listeners) {
				notifyFinishedUseCase(listener);
			}
			AbstractApplication.get().getAnalyticsSender().trackTiming("UseCase", getClass().getSimpleName(),
					getClass().getSimpleName(), executionTime);
		} catch (RuntimeException e) {
			AbstractException abstractException;
			if (e instanceof AbstractException) {
				abstractException = (AbstractException)e;
			} else {
				abstractException = new UnexpectedException(e);
			}
			abstractException.setPriorityLevel(exceptionPriorityLevel);
			markAsFailed(abstractException);
			for (T listener : listeners) {
				notifyFailedUseCase(abstractException, listener);
			}
		} finally {
			if (!listeners.isEmpty()) {
				markAsNotified();
			}
		}
	}
	
	/**
	 * @param minimumExecutionTime the minimumExecutionTime to set
	 */
	public void setMinimumExecutionTime(Long minimumExecutionTime) {
		this.minimumExecutionTime = minimumExecutionTime;
	}
	
	/**
	 * @return the executionTime
	 */
	public Long getExecutionTime() {
		return executionTime;
	}
	
	/**
	 * Override this method with the use case functionality to be executed
	 */
	@WorkerThread
	protected abstract void doExecute();
	
	/**
	 * Notifies the listener that the use case has started. <br/>
	 * You can override this method for a custom notification when your listeners may be listening to multiple use cases
	 * at a time.
	 * 
	 * @param listener The listener to notify.
	 */
	@WorkerThread
	protected abstract void notifyUseCaseStart(T listener);
	
	/**
	 * Notifies the listener that the use case has finished successfully. <br/>
	 * You can override this method for a custom notification when your listeners may be listening to multiple use cases
	 * at a time.
	 * 
	 * @param listener The listener to notify.
	 */
	@WorkerThread
	protected abstract void notifyFinishedUseCase(T listener);
	
	/**
	 * Notifies the listener that the use case has failed to execute. <br/>
	 * You can override this method for a custom notification when your listeners may be listening to multiple use cases
	 * at a time.
	 * 
	 * @param listener The listener to notify.
	 */
	@WorkerThread
	protected abstract void notifyFailedUseCase(AbstractException e, T listener);
	
	/**
	 * @return the listeners
	 */
	protected List<T> getListeners() {
		return listeners;
	}
	
	/**
	 * @param listener the listener to add
	 */
	@Override
	public void addListener(T listener) {
		if (!listeners.contains(listener)) {
			this.listeners.add(listener);
		}
	}
	
	/**
	 * @param listener the listener to remove
	 */
	@Override
	public void removeListener(T listener) {
		this.listeners.remove(listener);
	}
	
	public Boolean isNotInvoked() {
		return UseCaseStatus.NOT_INVOKED.equals(useCaseStatus);
	}
	
	public Boolean isInProgress() {
		return UseCaseStatus.IN_PROGRESS.equals(useCaseStatus);
	}
	
	public Boolean isFinishSuccessful() {
		return UseCaseStatus.FINISHED_SUCCESSFUL.equals(useCaseStatus);
	}
	
	public Boolean isFinishFailed() {
		return UseCaseStatus.FINISHED_FAILED.equals(useCaseStatus);
	}
	
	/**
	 * @return If the use case has finished, regardless of the success or failure of its execution.
	 */
	public Boolean isFinish() {
		return (isFinishFailed() || isFinishSuccessful());
	}
	
	public void markAsNotified() {
		notified = true;
	}
	
	public void markAsNotNotified() {
		notified = false;
	}
	
	public void markAsNotInvoked() {
		this.useCaseStatus = UseCaseStatus.NOT_INVOKED;
	}
	
	protected void markAsInProgress() {
		notified = false;
		abstractException = null;
		this.useCaseStatus = UseCaseStatus.IN_PROGRESS;
	}
	
	protected void markAsSuccessful() {
		this.useCaseStatus = UseCaseStatus.FINISHED_SUCCESSFUL;
	}
	
	protected void markAsFailed(AbstractException abstractException) {
		this.useCaseStatus = UseCaseStatus.FINISHED_FAILED;
		this.abstractException = abstractException;
	}
	
	public AbstractException getAbstractException() {
		return abstractException;
	}
	
	public Boolean isNotified() {
		return notified;
	}

	public void setExceptionPriorityLevel(int exceptionPriorityLevel) {
		this.exceptionPriorityLevel = exceptionPriorityLevel;
	}
}
