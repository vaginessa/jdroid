package com.jdroid.android;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;

@RunWith(CustomRobolectricRunner.class)
public class AbstractIntegrationTest {

	@Before
	public final void setUp() throws Exception {
		Robolectric.getFakeHttpLayer().interceptHttpRequests(false);
		onSetup();
	}

	protected void onSetup() {
		// Do nothing
	}
}
