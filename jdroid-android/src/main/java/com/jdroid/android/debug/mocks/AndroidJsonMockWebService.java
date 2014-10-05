package com.jdroid.android.debug.mocks;

import com.jdroid.android.AbstractApplication;
import com.jdroid.android.debug.CrashGenerator;
import com.jdroid.java.http.mock.JsonMockWebService;

public class AndroidJsonMockWebService extends JsonMockWebService {
	
	public static final String NONE = "None";
	
	public AndroidJsonMockWebService(Object... urlSegments) {
		super(urlSegments);
	}
	
	/**
	 * @see com.jdroid.java.http.mock.AbstractMockWebService#simulateCrash()
	 */
	@Override
	protected void simulateCrash() {
		String crashType = AbstractApplication.get().getAppContext().getHttpMockCrashType();
		if ((crashType != null) && !crashType.equals(NONE)) {
			CrashGenerator.crash(crashType, false);
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
