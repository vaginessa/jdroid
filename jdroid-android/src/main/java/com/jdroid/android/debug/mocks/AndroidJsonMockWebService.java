package com.jdroid.android.debug.mocks;

import com.jdroid.android.AbstractApplication;
import com.jdroid.android.debug.CrashGenerator;
import com.jdroid.android.debug.ExceptionType;
import com.jdroid.java.http.mock.JsonMockWebService;

public class AndroidJsonMockWebService extends JsonMockWebService {
	
	public AndroidJsonMockWebService(Object... urlSegments) {
		super(urlSegments);
	}
	
	/**
	 * @see com.jdroid.java.http.mock.AbstractMockWebService#simulateCrash()
	 */
	@Override
	protected void simulateCrash() {
		ExceptionType exceptionType = AbstractApplication.get().getAppContext().getHttpMockExceptionType();
		if (exceptionType != null) {
			CrashGenerator.crash(exceptionType, false);
		}
	}
	
	/**
	 * @see com.jdroid.java.http.mock.AbstractMockWebService#getHttpMockSleepDuration(java.lang.Object[])
	 */
	@Override
	protected Integer getHttpMockSleepDuration(Object... urlSegments) {
		return AbstractApplication.get().getAppContext().getHttpMockSleepDuration();
	}
	
}
