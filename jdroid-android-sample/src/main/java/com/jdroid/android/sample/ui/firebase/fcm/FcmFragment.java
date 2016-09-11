package com.jdroid.android.sample.ui.firebase.fcm;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.jdroid.android.fragment.AbstractFragment;
import com.jdroid.android.firebase.fcm.AbstractFcmAppModule;
import com.jdroid.android.firebase.fcm.FcmRegistrationCommand;
import com.jdroid.android.firebase.instanceid.InstanceIdHelper;
import com.jdroid.android.sample.R;
import com.jdroid.android.sample.api.SampleApiService;
import com.jdroid.java.collections.Maps;
import com.jdroid.java.concurrent.ExecutorUtils;
import com.jdroid.java.exception.UnexpectedException;

import java.io.IOException;
import java.util.Map;

public class FcmFragment extends AbstractFragment {

	@Override
	public Integer getContentFragmentLayout() {
		return R.layout.fcm_fragment;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		findView(R.id.registerDevice).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				new FcmRegistrationCommand().start(false);
			}
		});
		findView(R.id.registerDeviceAndUpdateLastActiveTimestamp).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				new FcmRegistrationCommand().start(true);
			}
		});

		findView(R.id.removeInstanceId).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ExecutorUtils.execute(new Runnable() {
					@Override
					public void run() {
						InstanceIdHelper.removeInstanceId();
					}
				});
			}
		});

		findView(R.id.removeDevice).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ExecutorUtils.execute(new Runnable() {
					@Override
					public void run() {
						new SampleApiService().removeDevice();
					}
				});
			}
		});

		final EditText messageKey = findView(R.id.messageKey);
		messageKey.setText("sampleMessage");

		final EditText minAppVersionCode = findView(R.id.minAppVersionCode);
		minAppVersionCode.setText("0");

		findView(R.id.sendPush).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ExecutorUtils.execute(new Runnable() {
					@Override
					public void run() {
						try {
							String registrationToken = FcmRegistrationCommand.getRegistrationToken(AbstractFcmAppModule.get().getFcmSenders().get(0).getSenderId());
							Map<String, String> params = Maps.newHashMap();
							if (minAppVersionCode.getText().length() > 0) {
								params.put("minAppVersionCode", minAppVersionCode.getText().toString());
							}
							new SampleApiService().sendPush(registrationToken, messageKey.getText().toString(), params);
						} catch (IOException e) {
							throw new UnexpectedException(e);
						}
					}
				});
			}
		});
	}
}
