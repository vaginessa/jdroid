package com.jdroid.android.sample;

import org.junit.Before;
import org.junit.runner.RunWith;

@RunWith(CustomRobolectricRunner.class)
public abstract class AbstractIntegrationTest {

	@Before
	public final void setUp() throws Exception {
		TestDebugContext.FAKE_HTTP_ENABLED = false;
		onSetup();
	}

	protected void onSetup() {
		// Do nothing
	}
}
