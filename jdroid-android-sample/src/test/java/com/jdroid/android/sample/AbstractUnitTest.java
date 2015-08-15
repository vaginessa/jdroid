package com.jdroid.android.sample;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;

@RunWith(CustomRobolectricRunner.class)
public abstract class AbstractUnitTest {

	@Before
	public final void setUp() throws Exception {
		Robolectric.getFakeHttpLayer().interceptHttpRequests(true);
		onSetup();
	}

	protected void onSetup() {
		// Do nothing
	}
}
