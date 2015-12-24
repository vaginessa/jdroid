package com.jdroid.android.sample.ui.service;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.jdroid.android.fragment.AbstractFragment;
import com.jdroid.android.sample.R;

public class ServiceFragment extends AbstractFragment {

	@Override
	public Integer getContentFragmentLayout() {
		return R.layout.service_fragment;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		findView(R.id.workerService).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.putExtra("a", "1");
				SampleWorkerService.runIntentInService(intent);
			}
		});
		findView(R.id.gcmTaskService).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Bundle bundle = new Bundle();
				bundle.putString("a", "2");
				SampleGcmTaskService.runIntentInService(bundle);
			}
		});
		findView(R.id.commandService1).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.putExtra("a", "3");
				new SampleServiceCommand1().start(intent);
			}
		});
		findView(R.id.commandService2).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.putExtra("a", "4");
				new SampleServiceCommand2().start(intent);
			}
		});
	}
}
