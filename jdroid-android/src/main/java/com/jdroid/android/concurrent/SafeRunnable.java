package com.jdroid.android.concurrent;

import com.jdroid.android.AbstractApplication;

/**
 * 
 * @author Maxi Rosson
 */
public abstract class SafeRunnable implements Runnable {
	
	/**
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public final void run() {
		try {
			doRun();
		} catch (RuntimeException e) {
			AbstractApplication.get().getExceptionHandler().logHandledException(e);
		}
	}
	
	public abstract void doRun();
}
