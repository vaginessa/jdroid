package com.jdroid.android.sample.ui.exceptions;

import android.os.Bundle;
import android.view.View;

import com.jdroid.android.exception.DefaultExceptionHandler;
import com.jdroid.android.exception.DialogErrorDisplayer;
import com.jdroid.android.exception.SnackbarErrorDisplayer;
import com.jdroid.android.fragment.AbstractFragment;
import com.jdroid.android.sample.R;
import com.jdroid.java.exception.AbstractException;
import com.jdroid.android.sample.usecase.FailingUseCase;

public class ExceptionHandlingFragment extends AbstractFragment {

	private FailingUseCase failingUseCase;
	
	@Override
	public Integer getContentFragmentLayout() {
		return R.layout.exception_handling_fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		failingUseCase = new FailingUseCase();
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		findView(R.id.defaultErrorDisplayer).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				failingUseCase.setErrorDisplayer(null);
				failingUseCase.setGoBackOnError(true);
				executeUseCase(failingUseCase);
			}
		});

		findView(R.id.defaultErrorDisplayerNotGoBack).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				failingUseCase.setErrorDisplayer(null);
				failingUseCase.setGoBackOnError(false);
				executeUseCase(failingUseCase);
			}
		});


		findView(R.id.snackbarErrorDisplayer).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				SnackbarErrorDisplayer errorDisplayer = new SnackbarErrorDisplayer();
				errorDisplayer.setParentLayoutId(R.id.container);
				errorDisplayer.setActionTextResId(R.string.retry);
				errorDisplayer.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						executeUseCase(failingUseCase);
					}
				});
				failingUseCase.setErrorDisplayer(errorDisplayer);
				executeUseCase(failingUseCase);
			}
		});
	}

	@Override
	public void onResume() {
		super.onResume();

		onResumeUseCase(failingUseCase, this);
	}

	@Override
	public void onPause() {
		super.onPause();

		onPauseUseCase(failingUseCase, this);
	}

	@Override
	public void onStartUseCase() {
		// Do nothing
	}

	@Override
	public void onFinishFailedUseCase(AbstractException abstractException) {
		DefaultExceptionHandler.setErrorDisplayer(abstractException, failingUseCase.getErrorDisplayer());

		if (failingUseCase.getErrorDisplayer() == null && !failingUseCase.getGoBackOnError()) {
			DialogErrorDisplayer.markAsNotGoBackOnError(abstractException);
		}

		throw abstractException;
	}
}
