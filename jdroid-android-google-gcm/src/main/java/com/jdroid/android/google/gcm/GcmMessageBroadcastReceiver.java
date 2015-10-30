package com.jdroid.android.google.gcm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;

import com.jdroid.android.application.AbstractApplication;
import com.jdroid.java.collections.Lists;

import java.util.List;

public abstract class GcmMessageBroadcastReceiver extends BroadcastReceiver {
	
	private static final String FROM = "from";

	@Override
	public void onReceive(Context context, Intent intent) {
		String from = intent.getStringExtra(FROM);
		GcmMessage gcmMessage = AbstractGcmAppModule.get().getGcmMessageResolver().resolve(from, intent.getExtras());
		onGcmMessage(gcmMessage, intent);
	}
	
	protected abstract void onGcmMessage(GcmMessage gcmMessage, Intent intent);
	
	public static void startListeningGcmBroadcasts(BroadcastReceiver broadcastReceiver, List<GcmMessage> gcmMessages) {
		IntentFilter intentFilter = new IntentFilter();
		for (GcmMessage gcmMessage : gcmMessages) {
			intentFilter.addAction(gcmMessage.getMessageKey());
		}
		LocalBroadcastManager.getInstance(AbstractApplication.get()).registerReceiver(broadcastReceiver, intentFilter);
	}
	
	public static void startListeningGcmBroadcasts(BroadcastReceiver broadcastReceiver, GcmMessage... gcmMessages) {
		startListeningGcmBroadcasts(broadcastReceiver, Lists.newArrayList(gcmMessages));
	}
	
	public static void stopListeningGcmBroadcasts(BroadcastReceiver broadcastReceiver) {
		if (broadcastReceiver != null) {
			LocalBroadcastManager.getInstance(AbstractApplication.get()).unregisterReceiver(broadcastReceiver);
		}
	}
	
	public static void sendBroadcast(GcmMessage gcmMessage, String from, Bundle data) {
		Intent broadcastIntent = new Intent();
		broadcastIntent.setAction(gcmMessage.getMessageKey());
		broadcastIntent.putExtra(FROM, from);
		broadcastIntent.putExtras(data);
		LocalBroadcastManager.getInstance(AbstractApplication.get()).sendBroadcast(broadcastIntent);
	}
}
