package com.jdroid.android.sample.ui.usecases;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.jdroid.android.exception.DialogErrorDisplayer;
import com.jdroid.android.fragment.AbstractFragment;
import com.jdroid.android.fragment.FragmentHelper;
import com.jdroid.android.sample.R;
import com.jdroid.java.exception.AbstractException;
import com.jdroid.java.exception.UnexpectedException;
import com.jdroid.java.utils.NumberUtils;

public class UseCasesFragment extends AbstractFragment {

	private SampleUseCase sampleUseCase;

	private static Boolean failExecution = false;
	private CheckBox failExecutionCheckBox;

	private static Boolean failStartNotification = false;
	private CheckBox failStartNotificationCheckBox;

	private static Boolean failFinishNotification = false;
	private CheckBox failFinishNotificationCheckBox;

	private static Boolean failFinishFailedNotification = false;
	private CheckBox failFinishFailedNotificationCheckBox;

	private static Boolean noListener = false;
	private CheckBox noListenerCheckBox;

	private static Integer delay = 5;
	private TextView delayTextView;

	private static FragmentHelper.UseCaseTrigger useCaseTrigger = FragmentHelper.UseCaseTrigger.MANUAL;
	private RadioGroup useCaseTriggerRadioGroup;

	private TextView useCaseStatus;

	@Override
	public Integer getContentFragmentLayout() {
		return R.layout.usecases_fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		sampleUseCase = new SampleUseCase();
		sampleUseCase.setDelayInSeconds(delay);
		sampleUseCase.setFail(failExecution);
	}

	@Override
	public void onStartUseCase() {
		if (failStartNotification) {
			throw new UnexpectedException("Failed start use case");
		} else {
			useCaseStatus.setText(R.string.onStartUseCase);
		}
	}

	@Override
	public void onFinishUseCase() {
		if (failFinishNotification) {
			throw new UnexpectedException("Failed finish use case");
		} else {
			useCaseStatus.setText(R.string.onFinishedUseCase);
		}
	}

	@Override
	public void onFinishFailedUseCase(AbstractException abstractException) {
		if (failFinishFailedNotification) {
			throw new UnexpectedException("Failed finish failed use case");
		} else {
			useCaseStatus.setText(R.string.onFinishedFailUseCase);
			DialogErrorDisplayer.markAsNotGoBackOnError(abstractException);
			super.onFinishFailedUseCase(abstractException);
		}
	}

	@Override
	public void onStart() {
		super.onStart();
		registerUseCase(sampleUseCase, noListener ? null : this, useCaseTrigger);
	}

	@Override
	public void onStop() {
		super.onStop();
		unregisterUseCase(sampleUseCase, this);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		failExecutionCheckBox = findView(R.id.failExecution);
		failExecutionCheckBox.setChecked(failExecution);
		failExecutionCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				failExecution = isChecked;
				sampleUseCase.setFail(failExecution);
			}
		});

		failStartNotificationCheckBox = findView(R.id.failStartNotification);
		failStartNotificationCheckBox.setChecked(failStartNotification);
		failStartNotificationCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				failStartNotification = isChecked;
			}
		});

		failFinishNotificationCheckBox = findView(R.id.failFinishNotification);
		failFinishNotificationCheckBox.setChecked(failFinishNotification);
		failFinishNotificationCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				failFinishNotification = isChecked;
			}
		});

		failFinishFailedNotificationCheckBox = findView(R.id.failFinishFailedNotification);
		failFinishFailedNotificationCheckBox.setChecked(failFinishFailedNotification);
		failFinishFailedNotificationCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				failFinishFailedNotification = isChecked;
			}
		});

		noListenerCheckBox = findView(R.id.noListener);
		noListenerCheckBox.setChecked(noListener);
		noListenerCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				noListener = isChecked;
			}
		});

		delayTextView = findView(R.id.delay);
		delayTextView.setText(delay.toString());

		useCaseTriggerRadioGroup = findView(R.id.useCaseTrigger);
		if (useCaseTrigger.equals(FragmentHelper.UseCaseTrigger.ALWAYS)) {
			((RadioButton)findView(R.id.alwaysUseCaseTrigger)).setChecked(true);
		} else if (useCaseTrigger.equals(FragmentHelper.UseCaseTrigger.MANUAL)) {
			((RadioButton)findView(R.id.manualUseCaseTrigger)).setChecked(true);
		} else if (useCaseTrigger.equals(FragmentHelper.UseCaseTrigger.ONCE)) {
			((RadioButton)findView(R.id.onceUseCaseTrigger)).setChecked(true);
		}
		useCaseTriggerRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if (checkedId == R.id.onceUseCaseTrigger) {
					useCaseTrigger = FragmentHelper.UseCaseTrigger.ONCE;
				} else if (checkedId == R.id.alwaysUseCaseTrigger) {
					useCaseTrigger = FragmentHelper.UseCaseTrigger.ALWAYS;
				} else if (checkedId == R.id.manualUseCaseTrigger) {
					useCaseTrigger = FragmentHelper.UseCaseTrigger.MANUAL;
				}
			}
		});

		findView(R.id.execute).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				delay = NumberUtils.getSafeInteger(delayTextView.getText().toString());
				sampleUseCase.setDelayInSeconds(delay);
				executeUseCase(sampleUseCase);
			}
		});

		useCaseStatus = findView(R.id.useCaseStatus);

	}
}
