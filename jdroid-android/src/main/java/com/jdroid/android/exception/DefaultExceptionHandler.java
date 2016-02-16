package com.jdroid.android.exception;

import android.support.annotation.NonNull;

import com.jdroid.android.R;
import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.context.UsageStats;
import com.jdroid.android.google.GooglePlayUtils;
import com.jdroid.android.utils.AndroidUtils;
import com.jdroid.android.utils.AppUtils;
import com.jdroid.android.utils.LocalizationUtils;
import com.jdroid.java.collections.Lists;
import com.jdroid.java.exception.AbstractException;
import com.jdroid.java.exception.ConnectionException;
import com.jdroid.java.exception.ErrorCode;
import com.jdroid.java.exception.ErrorCodeException;
import com.jdroid.java.utils.LoggerUtils;
import com.jdroid.java.utils.ReflectionUtils;
import com.jdroid.java.utils.StringUtils;

import org.slf4j.Logger;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.List;

public class DefaultExceptionHandler implements ExceptionHandler {
	
	private final static Logger LOGGER = LoggerUtils.getLogger(DefaultExceptionHandler.class);
	
	private static final String MAIN_THREAD_NAME = "main";
	private static final String ERROR_DISPLAYER = "errorDisplayer";

	private UncaughtExceptionHandler wrappedExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
	private UncaughtExceptionHandler defautlExceptionHandler;
	
	/**
	 * @see java.lang.Thread.UncaughtExceptionHandler#uncaughtException(java.lang.Thread, java.lang.Throwable)
	 */
	@Override
	public void uncaughtException(Thread thread, Throwable throwable) {
		
		Boolean mainThread = thread.getName().equals(MAIN_THREAD_NAME);
		
		if (mainThread) {
			handleMainThreadException(thread, throwable);
		} else if (DefaultExceptionHandler.matchAnyErrorCode(throwable, CommonErrorCode.INVALID_API_VERSION)) {
			handleInvalidApiVersionError();
		} else if (!doUncaughtException(thread, throwable)) {
			try {
				handleThrowable(thread, throwable);
			} catch (Exception e) {
				logHandledException("Error when trying to handle an exception", e);
			}
		}
	}
	
	protected Boolean doUncaughtException(Thread thread, Throwable throwable) {
		return false;
	}
	
	protected void handleMainThreadException(Thread thread, Throwable throwable) {
		try {
			try {
				UsageStats.setLastCrashTimestamp();
				List<String> tags = getThrowableTags(throwable, null);
				AbstractApplication.get().getAnalyticsSender().trackFatalException(throwable, tags);
			} catch (Exception e) {
				wrappedExceptionHandler.uncaughtException(thread, e);
			}
			wrappedExceptionHandler.uncaughtException(thread, throwable);
		} catch (Exception e) {
			LOGGER.error("Error when trying to handle an exception", e);
			defautlExceptionHandler.uncaughtException(thread, throwable);
		}
	}
	
	protected void handleInvalidApiVersionError() {
		GooglePlayUtils.showUpdateDialog();
	}
	
	/**
	 * @see com.jdroid.android.exception.ExceptionHandler#logWarningException(java.lang.String, java.lang.Throwable)
	 */
	@Override
	public void logWarningException(String errorMessage, Throwable throwable) {
		if (throwable instanceof ConnectionException) {
			logHandledException(throwable);
		} else {
			logHandledException(new WarningException(errorMessage, throwable));
		}
	}
	
	/**
	 * @see com.jdroid.android.exception.ExceptionHandler#logWarningException(java.lang.String)
	 */
	@Override
	public void logWarningException(String errorMessage) {
		logHandledException(new WarningException(errorMessage));
	}

	@Override
	public void logIgnoreStackTraceWarningException(String errorMessage) {
		logHandledException(new WarningException(errorMessage, true));
	}
	
	/**
	 * @see com.jdroid.android.exception.ExceptionHandler#logHandledException(java.lang.Throwable)
	 */
	@Override
	public void logHandledException(Throwable throwable) {
		logHandledException(null, throwable);
	}

	/**
	 * @see com.jdroid.android.exception.ExceptionHandler#logHandledException(java.lang.String, java.lang.Throwable)
	 */
	@Override
	public void logHandledException(String errorMessage, Throwable throwable) {
		
		if (throwable instanceof ConnectionException) {
			if (errorMessage == null) {
				errorMessage = "Connection error";
			}
			LOGGER.warn(errorMessage, throwable);
		} else {
			Boolean trackable = true;
			Throwable throwableToLog = throwable;
			int priorityLevel = AbstractException.NORMAL_PRIORITY;

			if (throwable instanceof AbstractException) {
				AbstractException abstractException = (AbstractException)throwable;
				trackable = abstractException.isTrackable();
				throwableToLog = abstractException.getThrowableToLog();
				priorityLevel = abstractException.getPriorityLevel();
			}
			
			if (errorMessage == null) {
				errorMessage = throwableToLog.getMessage();
				if (errorMessage == null) {
					errorMessage = "Error";
				}
			}
			
			if (trackable) {
				LOGGER.error(errorMessage, throwableToLog);
				List<String> tags = getThrowableTags(throwable, priorityLevel);
				AbstractApplication.get().getAnalyticsSender().trackHandledException(throwableToLog, tags);
			} else {
				LOGGER.warn(errorMessage);
			}
		}
	}
	
	public static void setMessageOnConnectionTimeout(RuntimeException runtimeException, String text) {
		if (runtimeException instanceof ConnectionException) {
			ConnectionException connectionException = (ConnectionException)runtimeException;
			if (connectionException.isReadTimeout()) {
				connectionException.setDescription(text);
			}
		}
		
	}
	
	public static void setMessageOnConnectionTimeout(RuntimeException runtimeException, int resId) {
		setMessageOnConnectionTimeout(runtimeException, LocalizationUtils.getString(resId));
	}
	
	/**
	 * @see com.jdroid.android.exception.ExceptionHandler#handleThrowable(java.lang.Throwable)
	 */
	@Override
	public void handleThrowable(Throwable throwable) {
		handleThrowable(Thread.currentThread(), throwable);
	}
	
	private void handleThrowable(Thread thread, Throwable throwable) {
		
		String title = null;
		String description = null;
		if (throwable instanceof AbstractException) {
			AbstractException abstractException = (AbstractException)throwable;
			title = abstractException.getTitle();
			description = abstractException.getDescription();
			if (((title == null) || (description == null)) && (abstractException instanceof ErrorCodeException)) {
				ErrorCodeException errorCodeException = (ErrorCodeException)abstractException;
				if (title == null) {
					title = LocalizationUtils.getTitle(errorCodeException.getErrorCode());
					if ((title == null)
							&& errorCodeException.getErrorCode().equals(
								com.jdroid.java.exception.CommonErrorCode.CONNECTION_ERROR)) {
						title = LocalizationUtils.getString(R.string.connectionErrorTitle);
					}
				}
				if (description == null) {
					description = LocalizationUtils.getDescription(errorCodeException.getErrorCode(),
						errorCodeException.getErrorCodeDescriptionArgs());
					if ((description == null)
							&& errorCodeException.getErrorCode().equals(
								com.jdroid.java.exception.CommonErrorCode.CONNECTION_ERROR)) {
						description = LocalizationUtils.getString(R.string.connectionErrorDescription);
					}
				}
			}
		}
		
		if (title == null) {
			title = LocalizationUtils.getString(R.string.defaultErrorTitle, AppUtils.getApplicationName());
		}
		
		if (description == null) {
			description = LocalizationUtils.getString(R.string.defaultErrorDescription);
		}

		displayError(title, description, throwable);

		logHandledException(throwable);
	}

	protected void displayError(String title, String description, Throwable throwable) {
		createErrorDisplayer(throwable).displayError(title, description, throwable);
	}

	@NonNull
	protected ErrorDisplayer createErrorDisplayer(Throwable throwable) {
		ErrorDisplayer errorDisplayer = null;
		if (throwable instanceof AbstractException) {
			AbstractException abstractException = (AbstractException)throwable;
			errorDisplayer = (ErrorDisplayer)abstractException.getParameter(ERROR_DISPLAYER);
		}
		if (errorDisplayer == null) {
			errorDisplayer = new DialogErrorDisplayer();
		}
		return errorDisplayer;
	}

	public static void setErrorDisplayer(AbstractException abstractException, ErrorDisplayer errorDisplayer) {
		abstractException.addParameter(ERROR_DISPLAYER, errorDisplayer);
	}
	
	public static Boolean matchAnyErrorCode(Throwable throwable, ErrorCode... errorCodes) {
		List<ErrorCode> errorCodesList = Lists.newArrayList(errorCodes);
		if (throwable instanceof ErrorCodeException) {
			ErrorCodeException errorCodeException = (ErrorCodeException)throwable;
			return errorCodesList.contains(errorCodeException.getErrorCode());
		}
		return false;
	}
	
	/**
	 * @see com.jdroid.android.exception.ExceptionHandler#setDefaultExceptionHandler(java.lang.Thread.UncaughtExceptionHandler)
	 */
	@Override
	public void setDefaultExceptionHandler(UncaughtExceptionHandler uncaughtExceptionHandler) {
		defautlExceptionHandler = uncaughtExceptionHandler;
	}

	protected List<String> getThrowableTags(Throwable throwable, Integer priorityLevel) {
		List<String> tags = Lists.newArrayList();
		if (priorityLevel != null) {
			tags.add("level" + String.format("%02d", priorityLevel));
		}
		return tags;
	}

	public static void addTags(Throwable throwable, List<String> tags) {
		StringBuilder builder = new StringBuilder();
		builder.append(StringUtils.join(tags, " "));

		if (throwable.getMessage() != null) {
			builder.append(" ");
			builder.append(throwable.getMessage());
		}

		try {
			ReflectionUtils.set(throwable, "detailMessage", builder.toString());
		} catch (Exception e) {
			// do nothing
		}
	}
	
}
