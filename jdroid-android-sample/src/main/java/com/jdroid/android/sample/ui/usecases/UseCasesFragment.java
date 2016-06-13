package com.jdroid.android.sample.ui.usecases;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.jdroid.android.exception.DialogErrorDisplayer;
import com.jdroid.android.fragment.AbstractFragment;
import com.jdroid.android.fragment.FragmentHelper;
import com.jdroid.android.sample.R;
import com.jdroid.android.utils.ToastUtils;
import com.jdroid.java.exception.AbstractException;

public class UseCasesFragment extends AbstractFragment {

	private SampleUseCase sampleUseCase;

	private static Boolean fail = false;
	private CheckBox failCheckBox;

	private static Boolean noListener = false;
	private CheckBox noListenerCheckBox;

	private static FragmentHelper.UseCaseTrigger useCaseTrigger = FragmentHelper.UseCaseTrigger.MANUAL;
	private RadioGroup useCaseTriggerRadioGroup;

	@Override
	public Integer getContentFragmentLayout() {
		return R.layout.usecases_fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		sampleUseCase = new SampleUseCase();
		sampleUseCase.setDelayInSeconds(5);
		sampleUseCase.setFail(fail);
	}

	@Override
	public void onStartUseCase() {
		ToastUtils.showToastOnUIThread(R.string.onStartUseCase);
	}

	@Override
	public void onFinishUseCase() {
		ToastUtils.showToastOnUIThread(R.string.onFinishedUseCase);
	}

	@Override
	public void onFinishFailedUseCase(AbstractException abstractException) {
		ToastUtils.showToastOnUIThread(R.string.onFinishedFailUseCase);
		DialogErrorDisplayer.markAsNotGoBackOnError(abstractException);
		super.onFinishFailedUseCase(abstractException);
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

		failCheckBox = findView(R.id.fail);
		failCheckBox.setChecked(fail);
		failCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				fail = isChecked;
				sampleUseCase.setFail(fail);
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
				executeUseCase(sampleUseCase);
			}
		});

	}
}
