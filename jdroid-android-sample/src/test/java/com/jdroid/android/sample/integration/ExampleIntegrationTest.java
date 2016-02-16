package com.jdroid.android.sample.integration;

import com.jdroid.android.sample.AbstractIntegrationTest;
import com.jdroid.android.sample.usecase.SampleItemsUseCase;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

public class ExampleIntegrationTest extends AbstractIntegrationTest {

	@Test
	public void example() {
		SampleItemsUseCase sampleItemsUseCase = new SampleItemsUseCase();
		sampleItemsUseCase.run();
		assertNotNull(sampleItemsUseCase.getItems());
		assertEquals(16, sampleItemsUseCase.getItems().size());
	}
}
