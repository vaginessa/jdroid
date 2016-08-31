package com.jdroid.android.sample.ui.exceptions;

import android.os.Bundle;
import android.view.View;

import com.jdroid.android.exception.AbstractErrorDisplayer;
import com.jdroid.android.exception.DialogErrorDisplayer;
import com.jdroid.android.exception.ErrorDisplayer;
import com.jdroid.android.exception.SnackbarErrorDisplayer;
import com.jdroid.android.fragment.AbstractFragment;
import com.jdroid.android.sample.R;
import com.jdroid.android.sample.ui.usecases.SampleUseCase;
import com.jdroid.java.exception.AbstractException;

public class ErrorDisplayerFragment extends AbstractFragment {

	private SampleUseCase failingUseCase;

	private ErrorDisplayer errorDisplayer;
	private Boolean goBackOnError = true;
	
	@Override
	public Integer getContentFragmentLayout() {
		return R.layout.exception_handling_fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		failingUseCase = new SampleUseCase();
		failingUseCase.setFail(true);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		findView(R.id.defaultErrorDisplayer).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ErrorDisplayerFragment.this.errorDisplayer = null;
				ErrorDisplayerFragment.this.goBackOnError = true;
				executeUseCase(failingUseCase);
			}
		});

		findView(R.id.defaultErrorDisplayerNotGoBack).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ErrorDisplayerFragment.this.errorDisplayer = null;
				ErrorDisplayerFragment.this.goBackOnError = false;
				executeUseCase(failingUseCase);
			}
		});


		findView(R.id.snackbarErrorDisplayer).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				SnackbarErrorDisplayer errorDisplayer = new SnackbarErrorDisplayer();
				errorDisplayer.setParentLayoutId(R.id.container);
				errorDisplayer.setActionTextResId(R.string.jdroid_retry);
				errorDisplayer.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						executeUseCase(failingUseCase);
					}
				});
				ErrorDisplayerFragment.this.errorDisplayer = errorDisplayer;
				executeUseCase(failingUseCase);
			}
		});
	}

	@Override
	public void onStart() {
		super.onStart();

		registerUseCase(failingUseCase, this);
	}

	@Override
	public void onStop() {
		super.onStop();

		unregisterUseCase(failingUseCase, this);
	}

	@Override
	public void onStartUseCase() {
		// Do nothing
	}

	@Override
	public ErrorDisplayer createErrorDisplayer(AbstractException abstractException) {
		AbstractErrorDisplayer.setErrorDisplayer(abstractException, errorDisplayer);
		if (errorDisplayer == null && !goBackOnError) {
			DialogErrorDisplayer.markAsNotGoBackOnError(abstractException);
		}
		return super.createErrorDisplayer(abstractException);
	}
}
