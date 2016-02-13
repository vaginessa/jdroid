package com.jdroid.android.sample.usecase;

import com.jdroid.android.exception.ErrorDisplayer;
import com.jdroid.android.usecase.AbstractUseCase;
import com.jdroid.java.exception.UnexpectedException;

public class FailingUseCase extends AbstractUseCase {

	private static final long serialVersionUID = -3206803568176386530L;

	private ErrorDisplayer errorDisplayer;
	private Boolean goBackOnError = true;

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

	public Boolean getGoBackOnError() {
		return goBackOnError;
	}

	public void setGoBackOnError(Boolean goBackOnError) {
		this.goBackOnError = goBackOnError;
	}
}