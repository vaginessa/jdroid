package com.jdroid.android.sample.unit;

import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.debug.http.HttpDebugConfiguration;
import com.jdroid.android.sample.AbstractUnitTest;
import com.jdroid.android.sample.TestAndroidApplication;
import com.jdroid.android.sample.TestAppContext;
import com.jdroid.android.sample.TestExceptionHandler;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;

public class ExampleUnitTest extends AbstractUnitTest {

	@Test
	public void example() {
		assertTrue(HttpDebugConfiguration.isHttpMockEnabled());
		assertEquals(TestAndroidApplication.class, AbstractApplication.get().getClass());
		assertNull(AbstractApplication.get().getHomeActivityClass());
		assertEquals(TestAppContext.class, AbstractApplication.get().getAppContext().getClass());
		assertFalse(AbstractApplication.get().getAppContext().isStrictModeEnabled());
		assertEquals(TestExceptionHandler.class, AbstractApplication.get().getExceptionHandler().getClass());
	}
}
