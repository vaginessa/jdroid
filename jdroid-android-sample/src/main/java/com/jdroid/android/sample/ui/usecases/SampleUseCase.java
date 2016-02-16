package com.jdroid.android.sample.ui.usecases;

import com.jdroid.android.usecase.AbstractUseCase;
import com.jdroid.java.concurrent.ExecutorUtils;
import com.jdroid.java.exception.UnexpectedException;

public class SampleUseCase extends AbstractUseCase {

	private Boolean fail = false;
	private Integer delayInSeconds = null;

	@Override
	protected void doExecute() {
		if (delayInSeconds != null) {
			ExecutorUtils.sleep(delayInSeconds);
		}
		if (fail) {
			throw new UnexpectedException("Sample use case failed");
		}
	}

	public Boolean getFail() {
		return fail;
	}

	public void setFail(Boolean fail) {
		this.fail = fail;
	}

	public Integer getDelayInSeconds() {
		return delayInSeconds;
	}

	public void setDelayInSeconds(Integer delayInSeconds) {
		this.delayInSeconds = delayInSeconds;
	}
}
