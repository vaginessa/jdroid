package com.jdroid.android.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import com.jdroid.java.date.DateUtils;
import com.jdroid.java.utils.IdGenerator;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class TimerView extends AppCompatTextView implements Handler.Callback {
	
	private static final int MESSAGE_CODE = IdGenerator.getIntId();
	private static final int HANDLER_DELAY = 1000;
	
	private Handler handler;
	private Boolean visible = false;
	
	private Long startTime;
	private Long stopTime;
	
	public TimerView(Context context, AttributeSet attrs) {
		super(context, attrs);
		handler = new Handler(this);
	}
	
	public TimerView(Context context) {
		this(context, null);
	}
	
	@Override
	public boolean handleMessage(Message msg) {
		updateTime();
		return true;
	}
	
	@SuppressLint("SetTextI18n")
	private void updateTime() {
		if (visible != null && visible) {
			if (startTime != null) {
				setText(formatDuration(getValue()));
				handler.sendMessageDelayed(Message.obtain(handler, MESSAGE_CODE), HANDLER_DELAY);
			} else {
				setText("00:00");
				handler.removeMessages(MESSAGE_CODE);
			}
		} else {
			handler.removeMessages(MESSAGE_CODE);
		}
	}
	
	@Override
	protected void onWindowVisibilityChanged(int visibility) {
		super.onWindowVisibilityChanged(visibility);
		visible = visibility == VISIBLE;
		updateTime();
	}
	
	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		visible = false;
		updateTime();
	}
	
	public void start() {
		startTime = DateUtils.nowMillis();
		stopTime = null;
		updateTime();
	}
	
	public void stop() {
		stopTime = DateUtils.nowMillis();
		updateTime();
	}
	
	public void reset() {
		startTime = null;
		stopTime = null;
		updateTime();
	}
	
	public Long getValue() {
		if (startTime != null && stopTime != null) {
			return stopTime - startTime;
		} else if (startTime != null) {
			return DateUtils.nowMillis() - startTime;
		} else {
			return null;
		}
	}
	
	private String formatDuration(long duration) {
		long hours = TimeUnit.MILLISECONDS.toHours(duration);
		long minutes = TimeUnit.MILLISECONDS.toMinutes(duration) - (hours * 60);
		long seconds = TimeUnit.MILLISECONDS.toSeconds(duration) - (hours * 60 * 60) - (minutes * 60);
		
		StringBuilder builder = new StringBuilder();
		builder.append(String.format(Locale.getDefault(), "%1$02d", minutes));
		builder.append(":");
		builder.append(String.format(Locale.getDefault(), "%1$02d", seconds));
		return builder.toString();
	}
	
}