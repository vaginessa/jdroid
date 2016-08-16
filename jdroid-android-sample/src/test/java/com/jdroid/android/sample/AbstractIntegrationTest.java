package com.jdroid.android.sample;

public abstract class AbstractIntegrationTest extends AbstractTest {

	@Override
	protected Boolean isHttpMockEnabled() {
		return false;
	}
}
