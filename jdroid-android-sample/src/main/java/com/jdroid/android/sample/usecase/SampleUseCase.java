package com.jdroid.android.sample.usecase;

import com.jdroid.android.usecase.DefaultAbstractUseCase;
import com.jdroid.java.collections.Lists;
import com.jdroid.java.concurrent.ExecutorUtils;

import java.util.List;

public class SampleUseCase extends DefaultAbstractUseCase {
	
	private static final long serialVersionUID = -3206803568176386530L;

	private List<String> items;
	/**
	 * @see com.jdroid.android.usecase.AbstractUseCase#doExecute()
	 */
	@Override
	protected void doExecute() {

		// Wait 3 seconds to simulate a request
		ExecutorUtils.sleep(3);

		items = Lists.newArrayList("one", "two", "three", "four", "five", "six", "seven", "eight", "nine", "ten", "eleven", "twelve", "thirteen", "fourteen", "fifteen", "sixteen");
	}

	public List<String> getItems() {
		return items;
	}
}
