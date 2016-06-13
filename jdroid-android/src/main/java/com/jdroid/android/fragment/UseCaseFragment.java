package com.jdroid.android.fragment;

import android.os.Bundle;

import com.jdroid.android.exception.DialogErrorDisplayer;
import com.jdroid.android.exception.ErrorDisplayer;
import com.jdroid.android.fragment.FragmentHelper.UseCaseTrigger;
import com.jdroid.android.usecase.AbstractUseCase;
import com.jdroid.java.exception.AbstractException;
import com.jdroid.java.utils.ReflectionUtils;

/**
 * 
 * @param <T>
 */
public abstract class UseCaseFragment<T extends AbstractUseCase> extends AbstractFragment {
	
	private T useCase;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
		
		useCase = ReflectionUtils.newInstance(getUseCaseClass());
		initializeUseCase(useCase);
	}
	
	protected void initializeUseCase(T useCase) {
		// Do Nothing
	}
	
	public T getUseCase() {
		return useCase;
	}
	
	protected abstract Class<T> getUseCaseClass();


	@Override
	public void onStart() {
		super.onStart();
		registerUseCase(useCase, this, getUseCaseTrigger());
	}

	@Override
	public void onStop() {
		super.onStop();
		unregisterUseCase(useCase, this);
	}
	
	protected UseCaseTrigger getUseCaseTrigger() {
		return UseCaseTrigger.ONCE;
	}
	
	public void executeUseCase() {
		executeUseCase(useCase);
	}

	@Override
	public ErrorDisplayer createErrorDisplayer(AbstractException abstractException) {
		DialogErrorDisplayer.markAsNotGoBackOnError(abstractException);
		return super.createErrorDisplayer(abstractException);
	}
}
