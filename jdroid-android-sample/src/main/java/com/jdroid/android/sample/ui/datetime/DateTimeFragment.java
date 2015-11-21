package com.jdroid.android.sample.ui.datetime;

import android.os.Bundle;
import android.view.View;

import com.jdroid.android.date.DatePickerDialogFragment;
import com.jdroid.android.date.TimePickerDialogFragment;
import com.jdroid.android.fragment.AbstractFragment;
import com.jdroid.android.sample.R;
import com.jdroid.java.date.DateUtils;

public class DateTimeFragment extends AbstractFragment {
	
	@Override
	public Integer getContentFragmentLayout() {
		return R.layout.datetime_fragment;
	}
	
	/**
	 * @see AbstractFragment#onViewCreated(View, Bundle)
	 */
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		findView(R.id.datePickerDialog).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				DatePickerDialogFragment.show(DateTimeFragment.this, DateUtils.now());
			}
		});

		findView(R.id.timePickerDialog).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				TimePickerDialogFragment.show(DateTimeFragment.this, DateUtils.now());
			}
		});

	}
}
