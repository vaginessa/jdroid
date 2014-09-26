package com.jdroid.sample.android.usecase;

import com.jdroid.android.usecase.DefaultAbstractUseCase;
import com.jdroid.java.concurrent.ExecutorUtils;

public class SampleUseCase extends DefaultAbstractUseCase {
	
	private static final long serialVersionUID = -3206803568176386530L;
	
	/**
	 * @see com.jdroid.android.usecase.AbstractUseCase#doExecute()
	 */
	@Override
	protected void doExecute() {
		ExecutorUtils.sleep(5);
	}
}
