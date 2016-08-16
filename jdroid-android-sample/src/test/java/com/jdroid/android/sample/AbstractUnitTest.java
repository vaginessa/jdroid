package com.jdroid.android.sample;

public abstract class AbstractUnitTest extends AbstractTest {

	@Override
	protected Boolean isHttpMockEnabled() {
		return true;
	}
}
