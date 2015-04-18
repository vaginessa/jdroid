package com.jdroid.sample.android.http;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jdroid.android.fragment.AbstractFragment;
import com.jdroid.java.concurrent.ExecutorUtils;
import com.jdroid.java.utils.LoggerUtils;
import com.jdroid.sample.android.R;
import com.jdroid.sample.android.api.SampleApiService;
import com.jdroid.sample.android.api.SampleResponse;

import org.slf4j.Logger;

public class HttpFragment extends AbstractFragment {

	private static final Logger LOGGER = LoggerUtils.getLogger(HttpFragment.class);

	/**
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup,
	 *      android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.http_fragment, container, false);
	}

	/**
	 * @see com.jdroid.android.fragment.AbstractFragment#onViewCreated(android.view.View, android.os.Bundle)
	 */
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		findView(R.id.httpGet).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ExecutorUtils.execute(new Runnable() {
					@Override
					public void run() {
						SampleResponse sampleResponse = new SampleApiService().httpGetSample();
						LOGGER.debug("Sample response key: " + sampleResponse.getSampleKey());
					}
				});
			}
		});

		findView(R.id.httpPost).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ExecutorUtils.execute(new Runnable() {
					@Override
					public void run() {
						new SampleApiService().httpPostSample();
					}
				});
			}
		});

		findView(R.id.httpPut).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ExecutorUtils.execute(new Runnable() {
					@Override
					public void run() {
						new SampleApiService().httpPutSample();
					}
				});
			}
		});

		findView(R.id.httpDelete).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ExecutorUtils.execute(new Runnable() {
					@Override
					public void run() {
						new SampleApiService().httpDeleteSample();
					}
				});
			}
		});

		findView(R.id.httpPatch).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ExecutorUtils.execute(new Runnable() {
					@Override
					public void run() {
						new SampleApiService().httpPatchSample();
					}
				});
			}
		});

	}
}
