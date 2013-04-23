package com.jdroid.android.common;

import org.junit.runners.model.InitializationError;
import android.app.Activity;
import android.app.Application;
import com.jdroid.android.AbstractApplication;
import com.xtremelabs.robolectric.Robolectric;
import com.xtremelabs.robolectric.RobolectricTestRunner;

public class AndroidTestRunner extends RobolectricTestRunner {
	
	public AndroidTestRunner(Class<?> testClass) throws InitializationError {
		super(testClass);
	}
	
	/**
	 * @see com.xtremelabs.robolectric.RobolectricTestRunner#prepareTest(java.lang.Object)
	 */
	@Override
	public void prepareTest(Object test) {
		Robolectric.application.onCreate();
		super.prepareTest(test);
	}
	
	@Override
	protected Application createApplication() {
		
		return new AbstractApplication() {
			
			@Override
			protected void initBitmapLruCache() {
			}
			
			@Override
			public Class<? extends Activity> getHomeActivityClass() {
				return null;
			}
		};
	}
}