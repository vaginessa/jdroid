package com.jdroid.sample.android.ui.analytics;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import com.jdroid.android.fragment.AbstractFragment;
import com.jdroid.sample.android.R;
import com.jdroid.sample.android.analytics.AndroidAnalyticsSender;

public class AnalyticsFragment extends AbstractFragment {
	
	/**
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup,
	 *      android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.analytics_fragment, container, false);
	}
	
	/**
	 * @see com.jdroid.android.fragment.AbstractFragment#onViewCreated(android.view.View, android.os.Bundle)
	 */
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		findView(R.id.sendExampleEvent).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				AndroidAnalyticsSender.get().trackExampleEvent();
			}
		});
	}
}
