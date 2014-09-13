package com.jdroid.android.debug;

import com.jdroid.android.exception.CommonErrorCode;
import com.jdroid.android.exception.DefaultExceptionHandler;
import com.jdroid.java.concurrent.ExecutorUtils;
import com.jdroid.java.exception.AbstractException;
import com.jdroid.java.exception.ApplicationException;
import com.jdroid.java.exception.BusinessException;
import com.jdroid.java.exception.ConnectionException;

public class CrashGenerator {
	
	private static final String CRASH_MESSAGE = "This is a generated crash for testing";
	
	public static void crash(final String crashType, Boolean executeOnNewThread) {
		Runnable runnable = new Runnable() {
			
			@Override
			public void run() {
				try {
					if (crashType.startsWith(BusinessException.class.getSimpleName())) {
						throw CommonErrorCode.INTERNAL_ERROR.newBusinessException(CRASH_MESSAGE);
					} else if (crashType.startsWith(ConnectionException.class.getSimpleName())) {
						throw new ConnectionException(CRASH_MESSAGE);
					} else if (crashType.startsWith(ApplicationException.class.getSimpleName())) {
						throw CommonErrorCode.SERVER_ERROR.newApplicationException(CRASH_MESSAGE);
					} else if (crashType.startsWith(RuntimeException.class.getSimpleName())) {
						throw new RuntimeException(CRASH_MESSAGE);
					}
				} catch (AbstractException e) {
					DefaultExceptionHandler.markAsNotGoBackOnError(e);
					throw e;
				}
			}
		};
		if (executeOnNewThread) {
			ExecutorUtils.execute(runnable);
		} else {
			runnable.run();
		}
	}
	
}
