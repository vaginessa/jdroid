package com.jdroid.android.usecase.service;

import android.annotation.SuppressLint;
import android.content.Intent;
import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.service.WorkerService;
import com.jdroid.android.usecase.DefaultAbstractUseCase;
import com.jdroid.android.notification.NotificationUtils;
import com.jdroid.java.concurrent.ExecutorUtils;

@SuppressLint("Registered")
public class UseCaseService extends WorkerService {
	
	private final static String USE_CASE = "useCase";
	private final static String NOTIFICATION_TO_CANCEL_ID = "notificationToCancelId";
	
	/**
	 * @see com.jdroid.android.service.WorkerService#doExecute(android.content.Intent)
	 */
	@Override
	protected void doExecute(Intent intent) {
		
		int notificationToCancelId = intent.getIntExtra(NOTIFICATION_TO_CANCEL_ID, 0);
		if (notificationToCancelId != 0) {
			NotificationUtils.cancelNotification(notificationToCancelId);
		}
		
		DefaultAbstractUseCase useCase = (DefaultAbstractUseCase)intent.getSerializableExtra(USE_CASE);
		useCase.run();
		
		if (useCase.isFinishFailed()) {
			AbstractApplication.get().getExceptionHandler().logHandledException(useCase.getAbstractException());
		}
	}
	
	public static void schedule(final DefaultAbstractUseCase useCase, Long delaySeconds) {
		ExecutorUtils.schedule(new Runnable() {
			
			@Override
			public void run() {
				execute(useCase);
			}
		}, delaySeconds);
	}
	
	public static void execute(DefaultAbstractUseCase useCase) {
		WorkerService.runIntentInService(AbstractApplication.get(), getServiceIntent(useCase, null),
			UseCaseService.class, false);
	}
	
	public static Intent getServiceIntent(DefaultAbstractUseCase useCase, Integer notificationToCancelId) {
		Intent intent = new Intent();
		intent.putExtra(USE_CASE, useCase);
		intent.putExtra(NOTIFICATION_TO_CANCEL_ID, notificationToCancelId);
		return WorkerService.getServiceIntent(AbstractApplication.get(), intent, UseCaseService.class, false);
	}
	
}
