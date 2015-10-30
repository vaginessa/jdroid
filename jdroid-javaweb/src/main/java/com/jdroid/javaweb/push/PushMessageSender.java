package com.jdroid.javaweb.push;

public interface PushMessageSender {
	
	public PushResponse send(PushMessage pushMessage);
	
}