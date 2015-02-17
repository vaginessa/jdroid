package com.jdroid.android.debug.mocks;

import android.preference.PreferenceManager;

import com.jdroid.android.AbstractApplication;
import com.jdroid.android.debug.CrashGenerator;
import com.jdroid.android.debug.ExceptionType;
import com.jdroid.java.http.mock.JsonMockWebService;

public class AndroidJsonMockWebService extends JsonMockWebService {

	public static final String HTTP_MOCK_CRASH_TYPE = "httpMockCrashType";

	public AndroidJsonMockWebService(Object... urlSegments) {
		super(urlSegments);
	}
	
	/**
	 * @see com.jdroid.java.http.mock.AbstractMockWebService#simulateCrash()
	 */
	@Override
	protected void simulateCrash() {
		ExceptionType exceptionType = getHttpMockExceptionType();
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

	private ExceptionType getHttpMockExceptionType() {
		return ExceptionType.find(PreferenceManager.getDefaultSharedPreferences(AbstractApplication.get()).getString(
				HTTP_MOCK_CRASH_TYPE, null));
	}
}
