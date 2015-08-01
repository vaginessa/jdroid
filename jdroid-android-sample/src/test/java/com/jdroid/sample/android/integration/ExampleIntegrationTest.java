package com.jdroid.sample.android.integration;

import com.jdroid.sample.android.AbstractIntegrationTest;
import com.jdroid.sample.android.usecase.SampleUseCase;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

public class ExampleIntegrationTest extends AbstractIntegrationTest {

	@Test
	public void example() {
		SampleUseCase sampleUseCase = new SampleUseCase();
		sampleUseCase.run();
		assertNotNull(sampleUseCase.getItems());
		assertEquals(16, sampleUseCase.getItems().size());
	}
}
