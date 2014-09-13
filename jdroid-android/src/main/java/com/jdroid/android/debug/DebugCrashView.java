package com.jdroid.android.debug;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import com.jdroid.android.R;
import com.jdroid.java.exception.ApplicationException;
import com.jdroid.java.exception.BusinessException;
import com.jdroid.java.exception.ConnectionException;

public class DebugCrashView extends LinearLayout {
	
	public DebugCrashView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}
	
	public DebugCrashView(Context context) {
		super(context);
		init(context);
	}
	
	private void init(Context context) {
		
		LayoutInflater.from(context).inflate(R.layout.debug_crash_view, this, true);
		
		findViewById(R.id.businessExceptionOnMainThread).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				CrashGenerator.crash(BusinessException.class.getSimpleName(), false);
			}
		});
		findViewById(R.id.businessExceptionOnWorkerThread).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				CrashGenerator.crash(BusinessException.class.getSimpleName(), true);
			}
		});
		findViewById(R.id.connectionExceptionOnMainThread).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				CrashGenerator.crash(ConnectionException.class.getSimpleName(), false);
			}
		});
		findViewById(R.id.connectionExceptionOnWorkerThread).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				CrashGenerator.crash(ConnectionException.class.getSimpleName(), true);
			}
		});
		findViewById(R.id.applicationExceptionOnMainThread).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				CrashGenerator.crash(ApplicationException.class.getSimpleName(), false);
			}
		});
		findViewById(R.id.applicationExceptionOnWorkerThread).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				CrashGenerator.crash(ApplicationException.class.getSimpleName(), true);
			}
		});
		findViewById(R.id.runtimeExceptionOnMainThread).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				CrashGenerator.crash(RuntimeException.class.getSimpleName(), false);
			}
		});
		findViewById(R.id.runtimeExceptionOnWorkerThread).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				CrashGenerator.crash(RuntimeException.class.getSimpleName(), true);
			}
		});
		
	}
}
