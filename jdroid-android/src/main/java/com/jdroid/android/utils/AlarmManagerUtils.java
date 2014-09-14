package com.jdroid.android.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import com.jdroid.android.AbstractApplication;

public class AlarmManagerUtils {
	
	public static void scheduleAlarm(int type, long triggerAtTime, PendingIntent operation) {
		getAlarmManager().set(type, triggerAtTime, operation);
	}
	
	public static void scheduleRtcWakeUpAlarm(long triggerAtTime, PendingIntent operation) {
		getAlarmManager().set(AlarmManager.RTC_WAKEUP, triggerAtTime, operation);
	}
	
	public static void cancelAlarm(PendingIntent operation) {
		getAlarmManager().cancel(operation);
	}
	
	private static AlarmManager getAlarmManager() {
		Context context = AbstractApplication.get();
		return (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
	}
}
