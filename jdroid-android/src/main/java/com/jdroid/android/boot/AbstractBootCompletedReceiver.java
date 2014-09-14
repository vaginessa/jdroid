package com.jdroid.android.boot;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.jdroid.android.AbstractApplication;

public abstract class AbstractBootCompletedReceiver extends BroadcastReceiver {
	
	@Override
	public void onReceive(Context context, Intent intent) {
		if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {
			try {
				onBootCompleted();
			} catch (Exception e) {
				AbstractApplication.get().getExceptionHandler().logHandledException(e);
			}
		}
	}
	
	protected abstract void onBootCompleted();
	
}
