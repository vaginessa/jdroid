package com.jdroid.sample.android.gcm;

import android.content.Intent;
import com.jdroid.android.gcm.GcmMessage;
import com.jdroid.android.utils.SharedPreferencesHelper;
import com.jdroid.android.utils.ToastUtils;

/**
 * GCM Message types
 * 
 * @author Maxi Rosson
 */
public enum AndroidGcmMessage implements GcmMessage {
	
	SAMPLE_MESSAGE("sampleMessage") {
		
		@Override
		public void handle(Intent intent) {
			ToastUtils.showInfoToast("Push received");
		}
	};
	
	private String messageKey;
	
	private AndroidGcmMessage(String messageKey) {
		this.messageKey = messageKey;
	}
	
	/**
	 * @see com.jdroid.android.gcm.GcmMessage#getMessageKey()
	 */
	@Override
	public String getMessageKey() {
		return messageKey;
	}
	
	public Boolean isNotificationEnabled() {
		return SharedPreferencesHelper.get().loadPreferenceAsBoolean(messageKey, true);
	}
	
}
