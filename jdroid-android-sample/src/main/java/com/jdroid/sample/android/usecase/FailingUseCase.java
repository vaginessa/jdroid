package com.jdroid.sample.android.usecase;

import com.jdroid.android.exception.ErrorDisplayer;
import com.jdroid.android.usecase.DefaultAbstractUseCase;
import com.jdroid.java.exception.UnexpectedException;

public class FailingUseCase extends DefaultAbstractUseCase {

	private static final long serialVersionUID = -3206803568176386530L;

	private ErrorDisplayer errorDisplayer;

	@Override
	protected void doExecute() {
		throw new UnexpectedException("Failing use case");
	}

	public void setErrorDisplayer(ErrorDisplayer errorDisplayer) {
		this.errorDisplayer = errorDisplayer;
	}

	public ErrorDisplayer getErrorDisplayer() {
		return errorDisplayer;
	}
}