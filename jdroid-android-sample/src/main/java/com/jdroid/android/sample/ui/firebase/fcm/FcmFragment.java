package com.jdroid.android.sample.ui.firebase.fcm;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.jdroid.android.firebase.fcm.AbstractFcmAppModule;
import com.jdroid.android.firebase.fcm.AbstractFcmMessageResolver;
import com.jdroid.android.firebase.fcm.FcmRegistrationCommand;
import com.jdroid.android.firebase.fcm.notification.NotificationMessage;
import com.jdroid.android.firebase.instanceid.InstanceIdHelper;
import com.jdroid.android.fragment.AbstractFragment;
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

		final EditText googleServerApiKeyEditText = findView(R.id.googleServerApiKey);
		googleServerApiKeyEditText.setText("AIzaSyBhf3imq3mldsdlh65nJqIIjXxLYPjh9fs");

		final EditText messageKeyEditText = findView(R.id.messageKey);
		messageKeyEditText.setText("sampleMessage");

		final EditText minAppVersionCode = findView(R.id.minAppVersionCode);
		minAppVersionCode.setText("0");

		final EditText minDeviceOsVersion = findView(R.id.minDeviceOsVersion);
		minDeviceOsVersion.setText("0");

		final EditText senderId = findView(R.id.senderId);
		senderId.setText(AbstractFcmAppModule.get().getFcmSenders().get(0).getSenderId());

		findView(R.id.sendPush).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ExecutorUtils.execute(new Runnable() {
					@Override
					public void run() {
						try {

							String googleServerApiKey = googleServerApiKeyEditText.getText().length() > 0 ? googleServerApiKeyEditText.getText().toString() : null;
							String registrationToken = FcmRegistrationCommand.getRegistrationToken(senderId.getText().toString());

							Map<String, String> params = Maps.newHashMap();
							if (minAppVersionCode.getText().length() > 0) {
								params.put(AbstractFcmMessageResolver.MIN_APP_VERSION_CODE_KEY, minAppVersionCode.getText().toString());
							}
							if (minDeviceOsVersion.getText().length() > 0) {
								params.put(AbstractFcmMessageResolver.MIN_DEVICE_OS_VERSION_KEY, minDeviceOsVersion.getText().toString());
							}

							String messageKey = messageKeyEditText.getText().toString();
							if (NotificationMessage.MESSAGE_KEY.equals(messageKey)) {
								params.put(NotificationMessage.TICKER, "Sample Ticker");
								params.put(NotificationMessage.CONTENT_TITLE, "Sample Content Title");
								params.put(NotificationMessage.CONTENT_TEXT, "Sample Content Text");
								params.put(NotificationMessage.LIGHT_ENABLED, "true");
								params.put(NotificationMessage.SOUND_ENABLED, "false");
								params.put(NotificationMessage.VIBRATION_ENABLED, "true");
								params.put(NotificationMessage.URL, "http://jdroidframework.com/uri/noflags?a=1");
								params.put(NotificationMessage.LARGE_ICON_URL, "http://jdroidframework.com/images/gradle.png");
							}

							new SampleApiService().sendPush(googleServerApiKey, registrationToken, messageKey, params);
						} catch (IOException e) {
							throw new UnexpectedException(e);
						}
					}
				});
			}
		});
	}
}
