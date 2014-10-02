package com.jdroid.android.debug;

import com.jdroid.android.AbstractApplication;
import com.jdroid.java.concurrent.ExecutorUtils;
import com.jdroid.java.repository.Repository;
import com.jdroid.java.utils.LoggerUtils;

public class DebugLogHelper {
	
	public static void log(final String text) {
		if (LoggerUtils.isEnabled()) {
			ExecutorUtils.execute(new Runnable() {
				
				@Override
				public void run() {
					
					try {
						Repository<DebugLog> repository = AbstractApplication.get().getRepositoryInstance(
							DebugLog.class);
						repository.add(new DebugLog(text));
					} catch (Exception e) {
						AbstractApplication.get().getExceptionHandler().logHandledException(e);
					}
				}
			});
		}
	}
}
